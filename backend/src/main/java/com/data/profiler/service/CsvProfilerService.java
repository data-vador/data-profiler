package com.data.profiler.service;

import com.amazon.deequ.VerificationSuite;
import com.amazon.deequ.analyzers.Analyzer;
import com.amazon.deequ.analyzers.runners.AnalysisRunner;
import com.amazon.deequ.analyzers.runners.AnalyzerContext;
import com.amazon.deequ.metrics.Metric;
import com.amazon.deequ.profiles.ColumnProfilerRunner;
import com.amazon.deequ.profiles.ColumnProfiles;
import com.amazon.deequ.profiles.NumericColumnProfile;
import com.amazon.deequ.profiles.StandardColumnProfile;
import com.data.profiler.model.ProfileResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;
import scala.Option;
import scala.collection.JavaConverters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvProfilerService {

    private SparkSession spark;

    public CsvProfilerService() {
        this.spark = SparkSession.builder()
                .appName("CSV Profiler")
                .master("local[*]")
                .config("spark.sql.adaptive.enabled", "false")
                .config("spark.sql.adaptive.coalescePartitions.enabled", "false")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");
    }

    public List<ProfileResult> profileCsv(String filePath) {
        try {
            // Lire le fichier CSV
            Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(filePath);

            // Utiliser Deequ pour le profilage
            ColumnProfiles profiles = ColumnProfilerRunner.onData(df).run();

            List<ProfileResult> results = new ArrayList<>();

            // Parcourir les profils de colonnes
            profiles.profiles().foreach(profile -> {
                String columnName = profile.column();
                StandardColumnProfile stdProfile = (StandardColumnProfile) profile;

                ProfileResult result = new ProfileResult();
                result.setColumnName(columnName);
                result.setDataType(stdProfile.dataType());
                result.setCompleteness(stdProfile.completeness());
                result.setDistinctCount(stdProfile.approximateNumDistinctValues());
                result.setNullCount(stdProfile.numNulls());
                result.setTotalCount(df.count());

                // Si c'est une colonne numérique, ajouter des statistiques supplémentaires
                if (profile instanceof NumericColumnProfile) {
                    NumericColumnProfile numProfile = (NumericColumnProfile) profile;
                    Option<Object> meanOpt = numProfile.mean();
                    Option<Object> stdDevOpt = numProfile.stdDev();
                    Option<Object> minOpt = numProfile.minimum();
                    Option<Object> maxOpt = numProfile.maximum();

                    if (meanOpt.isDefined()) {
                        result.setMean(((Number) meanOpt.get()).doubleValue());
                    }
                    if (stdDevOpt.isDefined()) {
                        result.setStdDev(((Number) stdDevOpt.get()).doubleValue());
                    }
                    if (minOpt.isDefined()) {
                        result.setMin(minOpt.get().toString());
                    }
                    if (maxOpt.isDefined()) {
                        result.setMax(maxOpt.get().toString());
                    }
                }

                results.add(result);
                return null; // Pour scala foreach
            });

            return results;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du profilage du fichier CSV: " + e.getMessage(), e);
        }
    }

    public void cleanup() {
        if (spark != null) {
            spark.close();
        }
    }
}
