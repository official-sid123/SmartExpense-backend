package com.example.demo.controller;

import com.example.demo.entity.Expense;
import com.example.demo.entity.Receipt;
import com.example.demo.entity.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.ReceiptRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OcrService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepo;
    private final ReceiptRepository receiptRepo;
    private final UserRepository userRepo;

    @Autowired
    private OcrService ocrService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ExpenseController(
            ExpenseRepository expenseRepo,
            ReceiptRepository receiptRepo,
            UserRepository userRepo
    ) {
        this.expenseRepo = expenseRepo;
        this.receiptRepo = receiptRepo;
        this.userRepo = userRepo;
    }

    // -----------------------------------------------------------------------
    // 1️⃣ CREATE NEW EXPENSE
    // -----------------------------------------------------------------------
    @PostMapping("/save")
    public ResponseEntity<?> createExpense(@RequestBody Expense expenseReq) {

        Optional<User> user = userRepo.findById(expenseReq.getUser().getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user");
        }

        Expense expense = new Expense();
//        expense.setUser(user.get());
        expense.setTitle(expenseReq.getTitle());
        expense.setTotalAmount(expenseReq.getTotalAmount());
        expense.setCategory(expenseReq.getCategory());
        expense.setCurrency(expenseReq.getCurrency());
        expense.setDateOfExpense(expenseReq.getDateOfExpense());
        expense.setNotes(expenseReq.getNotes());
        expense.setGstNumber(expenseReq.getGstNumber());
        expense.setGstAmount(expenseReq.getGstAmount());
        
        User employee = user.get();
        expense.setUser(employee);

        // VERY IMPORTANT: assign manager
        expense.setApprover(employee.getReportingManager());

        expense.setStatus("PENDING"); // not CREATED
        expense.setSubmittedAt(LocalDateTime.now());



        Expense saved = expenseRepo.save(expense);

        return ResponseEntity.ok(saved);
    }

    // -----------------------------------------------------------------------
    // 2️⃣ UPLOAD RECEIPT + OCR (TESSERACT)
    // -----------------------------------------------------------------------
    @PostMapping("/{expenseId}/receipts")
    public ResponseEntity<?> uploadReceiptToExpense(@PathVariable Long expenseId,
                                                    @RequestParam("file") MultipartFile file) {
        try {
            Optional<Expense> expenseOpt = expenseRepo.findById(expenseId);
            if (expenseOpt.isEmpty()) return ResponseEntity.badRequest().body("Expense not found");

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir + "/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // OCR (Tesseract)
            String text = ocrService.extractText(filePath.toFile());
            Map<String, Object> ocrResponse = parseReceipt(text);

            // Save receipt entity
            Receipt receipt = new Receipt();
            receipt.setExpense(expenseOpt.get());
            receipt.setFilePath(fileName);
            receipt.setUploadedAt(LocalDateTime.now());
            receipt.setOcrConfidence(0.0);
            receipt.setOcrRawJson(text);
            receiptRepo.save(receipt);

            return ResponseEntity.ok(Map.of("message", "Receipt uploaded", "ocr", ocrResponse));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // 3️⃣ AUTO-CREATE DRAFT + OCR
    // -----------------------------------------------------------------------
    @PostMapping("/receipts")
    public ResponseEntity<?> uploadReceiptCreateDraft(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId
    ) {
        try {
            Optional<User> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid userId");

            // Create DRAFT expense
            Expense draft = new Expense();
            draft.setUser(userOpt.get());
            draft.setStatus("DRAFT");
            draft.setSubmittedAt(LocalDateTime.now());
            Expense savedExpense = expenseRepo.save(draft);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir + "/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // OCR - Tesseract
            String text = ocrService.extractText(filePath.toFile());
            Map<String, Object> ocr = parseReceipt(text);

            // Save receipt
            Receipt receipt = new Receipt();
            receipt.setExpense(savedExpense);
            receipt.setFilePath(fileName);
            receipt.setUploadedAt(LocalDateTime.now());
            receipt.setOcrConfidence(0.0);
            receipt.setOcrRawJson(text);
            receiptRepo.save(receipt);

            return ResponseEntity.ok(Map.of(
                    "expenseId", savedExpense.getId(),
                    "ocr", ocr
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // 4️⃣ GET EXPENSE LIST
    // -----------------------------------------------------------------------
    @GetMapping("/list")
    public ResponseEntity<?> getMyExpenses(@RequestParam Long userId) {
        List<Expense> list = expenseRepo.findByUserId(userId);
        list.forEach(e -> {
            if (e.getReceipts() != null) {
                e.getReceipts().size(); // force lazy load initialization
            }
        });
        return ResponseEntity.ok(list);
    }

    // -----------------------------------------------------------------------
    // 5️⃣ UPDATE EXPENSE (Final Save)
    // -----------------------------------------------------------------------
    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(@PathVariable Long expenseId,
                                           @RequestBody Expense expenseReq) {
        try {
            Optional<Expense> opt = expenseRepo.findById(expenseId);
            if (opt.isEmpty()) return ResponseEntity.badRequest().body("Expense not found");

            Expense e = opt.get();

            e.setTitle(expenseReq.getTitle());
            e.setTotalAmount(expenseReq.getTotalAmount());
            e.setCategory(expenseReq.getCategory());
            e.setCurrency(expenseReq.getCurrency());
            e.setDateOfExpense(expenseReq.getDateOfExpense());
            e.setNotes(expenseReq.getNotes());
            e.setGstNumber(expenseReq.getGstNumber());
            e.setGstAmount(expenseReq.getGstAmount());

            // important
            e.setApprover(e.getUser().getReportingManager());

            // Final status
            e.setStatus("PENDING");
            e.setSubmittedAt(LocalDateTime.now());

            expenseRepo.save(e);

            return ResponseEntity.ok(e);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }


    // -----------------------------------------------------------------------
    // 6️⃣ OCR TEXT PARSING (Amount, Date, Vendor)
    // -----------------------------------------------------------------------
    private Map<String, Object> parseReceipt(String rawText) {

        Map<String, Object> map = new HashMap<>();

        // Amount
        String amountRegex = "(\\d+\\.\\d{2})";
        Matcher amountMatcher = Pattern.compile(amountRegex).matcher(rawText);
        String amount = amountMatcher.find() ? amountMatcher.group(1) : null;

        // Date
        String dateRegex = "(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})";
        Matcher dateMatcher = Pattern.compile(dateRegex).matcher(rawText);
        String date = dateMatcher.find() ? dateMatcher.group(1) : null;

        // Vendor = receipt first line
        String vendor = rawText.split("\n")[0];
        
        //gstnumber
        String gstNumRegex = "(GSTIN[:\\s]*[0-9A-Z]{15})";
        Matcher gstMatcher = Pattern.compile(gstNumRegex).matcher(rawText);

        String gstNumber = null;
        if (gstMatcher.find()) {
            gstNumber = gstMatcher.group(1).replace("GSTIN", "").trim();
        }
        
        //gstamount
        
        String gstAmountRegex = "(GST[^\\d]*(\\d+\\.\\d{2}))";
        Matcher gstAmtMatcher = Pattern.compile(gstAmountRegex).matcher(rawText);

        String gstAmount = gstAmtMatcher.find() ? gstAmtMatcher.group(2) : null;

        map.put("rawText", rawText);
        map.put("amount", amount);
        map.put("date", date);
        map.put("vendor", vendor);
        map.put("gstNumber", gstNumber);
        map.put("gstAmount", gstAmount);

        return map;
    }
    
    @PostMapping("/draft")
    public ResponseEntity<?> createDraftExpense(@RequestParam Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid userId");

        Expense draft = new Expense();
        draft.setUser(userOpt.get());
        draft.setStatus("DRAFT");
        draft.setSubmittedAt(LocalDateTime.now());

        Expense saved = expenseRepo.save(draft);
        return ResponseEntity.ok(Map.of("expenseId", saved.getId()));
    }
    
    
 // DELETE EXPENSE
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId) {
        Optional<Expense> opt = expenseRepo.findById(expenseId);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Expense not found");

        expenseRepo.delete(opt.get());
        return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
    }
}
