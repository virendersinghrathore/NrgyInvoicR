package com.hclc.nrgyinvoicr.backend.invoices.boundary;

import com.hclc.nrgyinvoicr.backend.EntityNotFoundException;
import com.hclc.nrgyinvoicr.backend.invoices.control.InvoiceRunsService;
import com.hclc.nrgyinvoicr.backend.invoices.entity.InvoiceRun;
import com.hclc.nrgyinvoicr.backend.invoices.entity.InvoiceRunsSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/invoiceRuns")
public class InvoiceRunsController {
    private final InvoiceRunsService invoiceRunsService;

    public InvoiceRunsController(InvoiceRunsService invoiceRunsService) {
        this.invoiceRunsService = invoiceRunsService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<InvoiceRun> createInvoiceRun(@RequestBody InvoiceRun invoiceRun) {
        InvoiceRun savedInvoiceRun = invoiceRunsService.createInvoiceRun(invoiceRun);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}").buildAndExpand(savedInvoiceRun.getId()).toUri();
        return ResponseEntity.created(uri).body(savedInvoiceRun);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<InvoiceRun> getInvoiceRun(@PathVariable Long id) {
        return invoiceRunsService.getInvoiceRun(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(InvoiceRun.class, id));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<InvoiceRun> findInvoiceRuns(InvoiceRunsSearchCriteria invoiceRunsSearchCriteria) {
        return invoiceRunsService.findInvoiceRuns(invoiceRunsSearchCriteria);
    }

    @GetMapping("/new")
    @Transactional(readOnly = true)
    public InvoiceRun prepareNewInvoiceRun() {
        return invoiceRunsService.prepareNewInvoiceRun();
    }

    @PostMapping("/{id}/start")
    @Transactional
    public ResponseEntity<InvoiceRun> start(@PathVariable Long id) throws Exception {
        InvoiceRun invoiceRun = invoiceRunsService.start(id);
        return ok(invoiceRun);
    }
}
