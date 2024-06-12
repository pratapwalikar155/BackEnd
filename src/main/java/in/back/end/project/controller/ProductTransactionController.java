package in.back.end.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class ProductTransactionController {
	@Autowired
    private ProductTransactionRepository repository;

    @GetMapping("/transactions")
    public List<ProductTransaction> getTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) throws ParseException {

        List<ProductTransaction> transactions;
        if (month != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            Date date = dateFormat.parse(month);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int monthValue = calendar.get(Calendar.MONTH) + 1;

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            transactions = repository.findByDateOfSaleBetween(startDate, endDate);
        } else {
            transactions = repository.findAll();
        }

        if (search != null) {
            transactions = transactions.stream().filter(transaction ->
                    transaction.getTitle().contains(search) ||
                    transaction.getDescription().contains(search) ||
                    Double.toString(transaction.getPrice()).contains(search)
            ).collect(Collectors.toList());
        }

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, transactions.size());

        return transactions.subList(start, end);
    }

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(@RequestParam String month) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        Date date = dateFormat.parse(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthValue = calendar.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        List<ProductTransaction> transactions = repository.findByDateOfSaleBetween(startDate, endDate);

       // double totalSaleAmount = transactions.stream().filter(ProductTransaction::isSold).mapToDouble(ProductTransaction::getPrice).sum();
        //long totalSoldItems = transactions.stream().filter(ProductTransaction::isSold).count();
       // long totalNotSoldItems = transactions.stream().filter(transaction -> !transaction.isSold()).count();

        Map<String, Object> stats = new HashMap<>();
        Object totalSaleAmount = null;
		stats.put("totalSaleAmount", totalSaleAmount);
        Object totalSoldItems = null;
		stats.put("totalSoldItems", totalSoldItems);
        Object totalNotSoldItems = null;
		stats.put("totalNotSoldItems", totalNotSoldItems);

        return stats;
    }

    @GetMapping("/bar_chart")
    public Map<String, Long> getBarChart(@RequestParam String month) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        Date date = dateFormat.parse(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthValue = calendar.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        List<ProductTransaction> transactions = repository.findByDateOfSaleBetween(startDate, endDate);

        Map<String, Long> priceRanges = new LinkedHashMap<>();
        priceRanges.put("0-100", 0L);
        priceRanges.put("101-200", 0L);
        priceRanges.put("201-300", 0L);
        priceRanges.put("301-400", 0L);
        priceRanges.put("401-500", 0L);
        priceRanges.put("501-600", 0L);
        priceRanges.put("601-700", 0L);
        priceRanges.put("701-800", 0L);
        priceRanges.put("801-900", 0L);
        priceRanges.put("901-above", 0L);

        for (ProductTransaction transaction : transactions) {
            double price = transaction.getPrice();
            if (price <= 100) priceRanges.put("0-100", priceRanges.get("0-100") + 1);
            else if (price <= 200) priceRanges.put("101-200", priceRanges.get("101-200") + 1);
            else if (price <= 300) priceRanges.put("201-300", priceRanges.get("201-300") + 1);
            else if (price <= 400) priceRanges.put("301-400", priceRanges.get("301-400") + 1);
            else if (price <= 500) priceRanges.put("401-500", priceRanges.get("401-500") + 1);
            else if (price <= 600) priceRanges.put("501-600", priceRanges.get("501-600") + 1);
            else if (price <= 700) priceRanges.put("601-700", priceRanges.get("601-700") + 1);
            else if (price <= 800) priceRanges.put("701-800", priceRanges.get("701-800") + 1);
            else if (price <= 900) priceRanges.put("801-900", priceRanges.get("801-900") + 1);
            else priceRanges.put("901-above", priceRanges.get("901-above") + 1);
        }

        return priceRanges;
    }

    @GetMapping("/pie_chart")
    public Map<String, Long> getPieChart(@RequestParam String month) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        Date date = dateFormat.parse(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthValue = calendar.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        List<ProductTransaction> transactions = repository.findByDateOfSaleBetween(startDate, endDate);
		return null;

       // return transactions.stream().collect(Collectors.groupingBy(ProductTransaction::getCategory, Collectors.counting()));
    }

    @GetMapping("/combined_data")
    public Map<String, Object> getCombinedData(@RequestParam String month) throws ParseException {
        Map<String, Object> combinedData = new HashMap<>();

        combinedData.put("transactions", getTransactions(null, month, 1, 10));
        combinedData.put("statistics", getStatistics(month));
        combinedData.put("bar_chart", getBarChart(month));
        combinedData.put("pie_chart", getPieChart(month));

        return combinedData;
    }
}

