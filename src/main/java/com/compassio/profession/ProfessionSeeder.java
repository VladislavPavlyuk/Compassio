package com.compassio.profession;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ProfessionSeeder implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(ProfessionSeeder.class);

    private final ProfessionRepository repository;
    private final ResourceLoader resourceLoader;
    private final boolean seedEnabled;
    private final String dataFile;

    public ProfessionSeeder(
            ProfessionRepository repository,
            ResourceLoader resourceLoader,
            @Value("${compassio.data.seed:true}") boolean seedEnabled,
            @Value("${compassio.data.file:classpath:data/SK ISCO-08_2020.xlsx}") String dataFile
    ) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.seedEnabled = seedEnabled;
        this.dataFile = dataFile;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!seedEnabled) {
            log.info("Seeding disabled.");
            return;
        }
        if (repository.count() > 0) {
            log.info("Profession table already has data; skipping seed.");
            return;
        }

        Resource resource = resourceLoader.getResource(dataFile);
        if (!resource.exists()) {
            throw new IllegalStateException("Seed file not found: " + dataFile);
        }

        Map<String, Profession> unique = new LinkedHashMap<>();
        DataFormatter formatter = new DataFormatter(Locale.forLanguageTag("sk-SK"));

        try (InputStream is = resource.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() < 2) {
                    continue; // title + header
                }
                String code = formatter.formatCellValue(row.getCell(0)).trim();
                String name = formatter.formatCellValue(row.getCell(1)).trim();
                if (code.isEmpty() || name.isEmpty()) {
                    continue;
                }
                String digits = code.replaceAll("\\D", "");
                int level = digits.isEmpty() ? 0 : digits.length();
                unique.putIfAbsent(code, new Profession(code, name, level));
            }
        }

        repository.saveAll(unique.values());
        log.info("Seeded {} professions from {}", unique.size(), dataFile);
    }
}
