-- File: src/main/resources/schema.sql

-- Create test data for development
INSERT INTO Product (productId, producerId, productName, status)
VALUES (1, 1, 'Storage Service', 'ACTIVE');

INSERT INTO RatePlan (
    ratePlanId, productId, currencyId, ratePlanName,
    ratePlanType, startDate, endDate, status
)
VALUES (
           1, 1, 1, 'Basic Storage Plan',
           'FLAT_RATE', '2024-01-01', '2024-12-31', 'ACTIVE'
       );

INSERT INTO RatePlanFlatRate (
    ratePlanId, ratePlanFlatDescription,
    unitType, unitMeasurement, flatRateUnitCalculation,
    unitRate, maxLimit
)
VALUES (
           1, 'Basic Storage Rate',
           'DATA_STORAGE', 'GB', 'MONTHLY',
           0.05, 1000.00
       );
