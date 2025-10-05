package org.example.bs.Service;

import org.example.bs.Repository.SalesRepository;
import org.example.bs.Model.Sales;

import java.sql.SQLException;
import java.util.List;

public class SalesService {
    private final SalesRepository salesRepository;

    public SalesService() {
        this.salesRepository = new SalesRepository();
    }

    public List<Sales> getAllSales() throws SQLException {
        return salesRepository.findAllSales();
    }

    public double calculateTotalIncome() throws SQLException {
        return salesRepository.getTotalIncome();
    }
}
