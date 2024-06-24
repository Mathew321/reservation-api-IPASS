package org.hu.reservation.controller;

import org.hu.reservation.entity.Table;
import org.hu.reservation.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public List<Table> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/{id}")
    public Table getTableById(@PathVariable Long id) {
        return tableService.getTableById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    @PostMapping
    public Table createTable(@RequestBody Table table) {
        return tableService.saveTable(table);
    }

    @PutMapping("/{id}")
    public Table updateTable(@PathVariable Long id, @RequestBody Table table) {
        table.setId(id); // Ensure the ID is set for update
        return tableService.saveTable(table);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
    }
}
