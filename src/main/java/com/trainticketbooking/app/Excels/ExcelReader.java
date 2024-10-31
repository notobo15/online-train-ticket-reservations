package com.trainticketbooking.app.Excels;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Services.impl.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class ExcelReader {

    @Autowired
    private StationService stationService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private RailwayNetworkService railwayNetworkService;

    @Autowired
    private PassengerTypeService passengerTypeService;

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private TrainJourneyService trainJourneyService;

    @Autowired
    private SeatTypeService seatTypeService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private CarriageClassService carriageClassService;

    @Autowired
    private CarriageSeatMappingService carriageSeatMappingService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private RouteService routeService;

    @PostConstruct
    public void readExcelOnStartup() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("data/data.xlsx");
            InputStream inputStream = classPathResource.getInputStream();

            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet provinceSheet = workbook.getSheetAt(3);
                readAndInsertProvinces(provinceSheet);

                Sheet stationSheet = workbook.getSheetAt(1);
                readAndInsertStations(stationSheet);

                Sheet railwayNetworkSheet = workbook.getSheetAt(6);
                readAndInsertRailwayNetworks(railwayNetworkSheet);

                Sheet trainSheet = workbook.getSheetAt(2);
                readAndInsertTrains(trainSheet);

                Sheet passengerTypeSheet = workbook.getSheetAt(14);
                readAndInsertPassengerTypes(passengerTypeSheet);

                Sheet carriageClassesSheet = workbook.getSheetAt(5);
                readAndInsertCarriageClasses(carriageClassesSheet);

                Sheet carriageSheet = workbook.getSheetAt(4);
                readAndInsertCarriages(carriageSheet);

                Sheet trainJourneySheet = workbook.getSheetAt(10);
                readAndInsertTrainJourneys(trainJourneySheet);

                Sheet seatTypeSheet = workbook.getSheetAt(9);
                readAndInsertSeatTypes(seatTypeSheet);

                Sheet priceSheet = workbook.getSheetAt(13);
                readAndInsertPrices(priceSheet);

                Sheet seatsSheet = workbook.getSheetAt(8);
                readAndInsertSeats(seatsSheet);

                Sheet carriageSeatMappingsSheet = workbook.getSheetAt(15);
                readAndInsertCarriageSeatMappings(carriageSeatMappingsSheet);

                Sheet routeSheet = workbook.getSheetAt(12);
                readAndInsertRoutes(routeSheet);
            }


        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void readAndInsertStations(Sheet sheet) {
        List<Station> stations = stationService.getAll();
        if (stations.isEmpty()) {
            System.out.println("Table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String stationIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String stationName = dataFormatter.formatCellValue(row.getCell(1));
                String provinceIdStr = dataFormatter.formatCellValue(row.getCell(3));
                String code = dataFormatter.formatCellValue(row.getCell(4));

                try {
                    Integer stationId = Integer.parseInt(stationIdStr);
                    Integer provinceId = Integer.parseInt(provinceIdStr);

                    Optional<Province> province = provinceService.getById(provinceId);

                    if (province.isPresent()) {
                        Station station = new Station();
                        station.setStationId(stationId);
                        station.setCode(code);
                        station.setStationName(stationName);
                        station.setProvince(province.get());

                        stationService.save(station);
                    } else {
                        System.out.println("Province with ID " + provinceId + " not found. Skipping this station.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Table already has data. No insertion needed.");
        }
    }

    public void readAndInsertProvinces(Sheet sheet) {
        List<Province> provinces = provinceService.getAll();
        if (provinces.isEmpty()) {
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String provinceIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String codeName = dataFormatter.formatCellValue(row.getCell(1));
                String fullName = dataFormatter.formatCellValue(row.getCell(2));
                String fullNameEn = dataFormatter.formatCellValue(row.getCell(3));
                String name = dataFormatter.formatCellValue(row.getCell(4));
                String nameEn = dataFormatter.formatCellValue(row.getCell(5));

                try {
                    Integer provinceId = Integer.parseInt(provinceIdStr);

                    Province province = new Province();
                    province.setProvinceId(provinceId);
                    province.setCodeName(codeName);
                    province.setFullName(fullName);
                    province.setFullNameEn(fullNameEn);
                    province.setName(name);
                    province.setNameEn(nameEn);

                    provinceService.save(province);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid province ID format: " + e.getMessage());
                }
            }
        }
    }

    public void readAndInsertTrains(Sheet sheet) {
        List<Train> trains = trainService.getAll();
        if (trains.isEmpty()) {
            System.out.println("Train table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String trainIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String trainNumber = dataFormatter.formatCellValue(row.getCell(1));
                String trainType = dataFormatter.formatCellValue(row.getCell(3));
                String railwayNetworkIdStr = dataFormatter.formatCellValue(row.getCell(4));

                try {
                    Integer trainId = Integer.parseInt(trainIdStr);
                    Integer railwayNetworkId = Integer.parseInt(railwayNetworkIdStr);

                    Optional<RailwayNetwork> railwayNetwork = railwayNetworkService.getById(railwayNetworkId);

                    if (railwayNetwork.isPresent()) {
                        Train train = new Train();
                        train.setTrainId(trainId);
                        train.setTrainNumber(trainNumber);
                        train.setTrainType(trainType);
                        train.setRailwayNetwork(railwayNetwork.get());

                        trainService.save(train);
                    } else {
                        System.out.println("Railway network with ID " + railwayNetworkId + " not found. Skipping this train.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Train table already has data. No insertion needed.");
        }
    }

    public void readAndInsertRailwayNetworks(Sheet sheet) {
        List<RailwayNetwork> railwayNetworks = railwayNetworkService.getAll();
        if (railwayNetworks.isEmpty()) {
            System.out.println("RailwayNetwork table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String railwayNetworkIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String name = dataFormatter.formatCellValue(row.getCell(1));

                try {
                    Integer railwayNetworkId = Integer.parseInt(railwayNetworkIdStr);

                    RailwayNetwork railwayNetwork = new RailwayNetwork();
                    railwayNetwork.setRailwayId(railwayNetworkId);
                    railwayNetwork.setName(name);
                    railwayNetwork.setStatus("Active");

                    railwayNetworkService.save(railwayNetwork);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("RailwayNetwork table already has data. No insertion needed.");
        }
    }

    public void readAndInsertPassengerTypes(Sheet sheet) {
        List<PassengerType> passengerTypes = passengerTypeService.getAll();
        if (passengerTypes.isEmpty()) {
            System.out.println("PassengerType table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String passengerTypeIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String passengerTypeName = dataFormatter.formatCellValue(row.getCell(1));
                String discountPercentageStr = dataFormatter.formatCellValue(row.getCell(2));

                try {
                    Integer passengerTypeId = Integer.parseInt(passengerTypeIdStr);
                    Double discountPercentage = Double.parseDouble(discountPercentageStr);

                    PassengerType passengerType = new PassengerType();
                    passengerType.setPassengerTypeId(passengerTypeId);
                    passengerType.setPassengerType(passengerTypeName);
                    passengerType.setDiscountPercentage(discountPercentage);

                    passengerTypeService.save(passengerType);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("PassengerType table already has data. No insertion needed.");
        }
    }

    public void readAndInsertCarriages(Sheet sheet) {
        List<Carriage> carriages = carriageService.getAll(); // Assuming CarriageService has a getAll method
        if (carriages.isEmpty()) {
            System.out.println("Carriage table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String carriageIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String seatCountStr = dataFormatter.formatCellValue(row.getCell(1));
                String totalFloorsStr = dataFormatter.formatCellValue(row.getCell(2));
                String trainIdStr = dataFormatter.formatCellValue(row.getCell(3));
                String carriageClassIdStr = dataFormatter.formatCellValue(row.getCell(4));
                String carNumber = dataFormatter.formatCellValue(row.getCell(5));


                try {
                    Integer carriageId = Integer.parseInt(carriageIdStr);
                    Integer trainId = Integer.parseInt(trainIdStr);
                    Integer carriageClassId = Integer.parseInt(carriageClassIdStr);
                    int seatCount = Integer.parseInt(seatCountStr);
                    int totalFloors = Integer.parseInt(totalFloorsStr);

                    Optional<Train> train = trainService.getById(trainId);
                    Optional<CarriageClass> carriageClass = carriageClassService.getById(carriageClassId);

                    if (train.isPresent() && carriageClass.isPresent()) {
                        Carriage carriage = new Carriage();
                        carriage.setCarriageId(carriageId);
                        carriage.setTrain(train.get());
                        carriage.setCarriageClass(carriageClass.get());
                        carriage.setCarNumber(carNumber);
                        carriage.setSeatCount(seatCount);
                        carriage.setTotalFloors(totalFloors);

                        carriageService.save(carriage);
                    } else {
                        System.out.println("Train or CarriageClass not found for Carriage ID " + carriageId + ". Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Carriage table already has data. No insertion needed.");
        }
    }

    public void readAndInsertCarriageClasses(Sheet sheet) {
        List<CarriageClass> carriageClasses = carriageClassService.getAll();
        if (carriageClasses.isEmpty()) {
            System.out.println("CarriageClass table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();

            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                String carriageClassIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String name = dataFormatter.formatCellValue(row.getCell(1));

                try {
                    Integer carriageClassId = Integer.parseInt(carriageClassIdStr);

                    CarriageClass carriageClass = new CarriageClass();
                    carriageClass.setCarriageClassId(carriageClassId);
                    carriageClass.setName(name);

                    carriageClassService.save(carriageClass);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("CarriageClass table already has data. No insertion needed.");
        }
    }

    public void readAndInsertTrainJourneys(Sheet sheet) {
        List<TrainJourney> trainJourneys = trainJourneyService.getAll();
        if (trainJourneys.isEmpty()) {
            System.out.println("TrainJourney table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String trainJourneyIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String departureDateStr = dataFormatter.formatCellValue(row.getCell(1));
                String arrivalDateStr = dataFormatter.formatCellValue(row.getCell(2));
                String status = dataFormatter.formatCellValue(row.getCell(3));
                String trainIdStr = dataFormatter.formatCellValue(row.getCell(4));


                try {
                    Integer trainJourneyId = Integer.parseInt(trainJourneyIdStr);
                    Integer trainId = Integer.parseInt(trainIdStr);
                    LocalDate departureDate = LocalDate.parse(departureDateStr, formatter);
                    LocalDate arrivalDate = arrivalDateStr.isEmpty() ? null : LocalDate.parse(arrivalDateStr);

                    Optional<Train> train = trainService.getById(trainId);

                    if (train.isPresent()) {
                        TrainJourney trainJourney = new TrainJourney();
                        trainJourney.setTrainJourneyId(trainJourneyId);
                        trainJourney.setTrain(train.get());
                        trainJourney.setDepartureDate(departureDate);
                        trainJourney.setArrivalDate(arrivalDate);
                        trainJourney.setStatus(status);

                        trainJourneyService.save(trainJourney);
                    } else {
                        System.out.println("Train not found for TrainJourney ID " + trainJourneyId + ". Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("TrainJourney table already has data. No insertion needed.");
        }
    }

    public void readAndInsertSeatTypes(Sheet sheet) {
        List<SeatType> seatTypes = seatTypeService.getAll();
        if (seatTypes.isEmpty()) {
            System.out.println("SeatType table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String seatTypeIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String seatTypeName = dataFormatter.formatCellValue(row.getCell(1));
                String code = dataFormatter.formatCellValue(row.getCell(2));
                String description = dataFormatter.formatCellValue(row.getCell(3));

                try {
                    Long seatTypeId = Long.parseLong(seatTypeIdStr);

                    SeatType seatType = new SeatType();
                    seatType.setSeatTypeId(seatTypeId);
                    seatType.setSeatType(seatTypeName);
                    seatType.setCode(code);
                    seatType.setDescription(description);

                    seatTypeService.save(seatType);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("SeatType table already has data. No insertion needed.");
        }
    }

    public void readAndInsertCarriageSeatMappings(Sheet sheet) {
        List<CarriageSeatMapping> carriageSeatMappings = carriageSeatMappingService.getAll();
        if (carriageSeatMappings.isEmpty()) {
            System.out.println("CarriageSeatMapping table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                String carriageSeatIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String carriageIdStr = dataFormatter.formatCellValue(row.getCell(1));
                String seatIdStr = dataFormatter.formatCellValue(row.getCell(2));
                String statusStr = dataFormatter.formatCellValue(row.getCell(3));

                try {
                    Integer carriageSeatId = Integer.parseInt(carriageSeatIdStr);
                    Integer carriageId = Integer.parseInt(carriageIdStr);
                    Integer seatId = Integer.parseInt(seatIdStr);
                    boolean status = Boolean.parseBoolean(statusStr);

                    // Fetch related Carriage and Seat entities
                    Optional<Carriage> carriage = carriageService.getById(carriageId);
                    Optional<Seat> seat = seatService.getById(seatId);

                    if (carriage.isPresent() && seat.isPresent()) {
                        CarriageSeatMapping mapping = new CarriageSeatMapping();
                        mapping.setCarriageSeatId(carriageSeatId);
                        mapping.setCarriage(carriage.get());
                        mapping.setSeat(seat.get());
                        mapping.setStatus(status);

                        carriageSeatMappingService.save(mapping);
                    } else {
                        System.out.println("Carriage or Seat not found for CarriageSeatMapping ID " + carriageSeatId + ". Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("CarriageSeatMapping table already has data. No insertion needed.");
        }
    }

    public void readAndInsertPrices(Sheet sheet) {
        List<Price> prices = priceService.getAll();
        if (prices.isEmpty()) {
            System.out.println("Price table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                String priceIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String pricePerKmStr = dataFormatter.formatCellValue(row.getCell(2));
                String surchargePercentageStr = dataFormatter.formatCellValue(row.getCell(3));
                String seatTypeIdStr = dataFormatter.formatCellValue(row.getCell(4));

                try {
                    Integer priceId = Integer.parseInt(priceIdStr);
                    Integer seatTypeId = Integer.parseInt(seatTypeIdStr);
                    Double surchargePercentage = surchargePercentageStr.isEmpty() ? null : Double.parseDouble(surchargePercentageStr);
                    Double pricePerKm = Double.parseDouble(pricePerKmStr);

                    Optional<SeatType> seatType = seatTypeService.getById(seatTypeId);

                    if (seatType.isPresent()) {
                        Price price = new Price();
                        price.setPriceId(priceId);
                        price.setSeatType(seatType.get());
                        price.setSurchargePercentage(surchargePercentage);
                        price.setPricePerKm(pricePerKm);

                        priceService.save(price);
                    } else {
                        System.out.println("SeatType not found for Price ID " + priceId + ". Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Price table already has data. No insertion needed.");
        }
    }
    public void readAndInsertSeats(Sheet sheet) {
        List<Seat> seats = seatService.getAll();
        if (seats.isEmpty()) {
            System.out.println("Seat table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                String seatIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String compartmentNumberStr = dataFormatter.formatCellValue(row.getCell(1));
                String floorStr = dataFormatter.formatCellValue(row.getCell(2));
                String seatNumber = dataFormatter.formatCellValue(row.getCell(3));
//                String status = dataFormatter.formatCellValue(row.getCell(4));
                String seatTypeIdStr = dataFormatter.formatCellValue(row.getCell(5));

                try {
                    Integer seatId = Integer.parseInt(seatIdStr);
                    Integer floor = Integer.parseInt(floorStr);
                    Integer compartmentNumber = Integer.parseInt(compartmentNumberStr);
                    Integer seatTypeId = Integer.parseInt(seatTypeIdStr);

                    Optional<SeatType> seatType = seatTypeService.getById(seatTypeId);

                    if (seatType.isPresent()) {
                        Seat seat = new Seat();
                        seat.setSeatId(seatId);
                        seat.setSeatNumber(seatNumber);
                        seat.setCompartmentNumber(compartmentNumber);
                        seat.setFloor(floor);
                        seat.setSeatType(seatType.get());
                        seat.setStatus(false);
                        seatService.save(seat);
                    } else {
                        System.out.println("Carriage or SeatType not found for Seat ID " + seatId + ". Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Seat table already has data. No insertion needed.");
        }
    }

    public void readAndInsertRoutes(Sheet sheet) {
        List<Route> routes = routeService.getAll();
        if (routes.isEmpty()) {
            System.out.println("Route table is empty, starting data insertion...");
            DataFormatter dataFormatter = new DataFormatter();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("[H:mm[:ss]]");

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                String routeIdStr = dataFormatter.formatCellValue(row.getCell(0));
                String arrivalTimeStr = dataFormatter.formatCellValue(row.getCell(1));
                String dateNumberStr = dataFormatter.formatCellValue(row.getCell(2));
                String departureTimeStr = dataFormatter.formatCellValue(row.getCell(3));
                String distanceStr = dataFormatter.formatCellValue(row.getCell(4));
                String stationNumberStr = dataFormatter.formatCellValue(row.getCell(5));
                String startStationIdStr = dataFormatter.formatCellValue(row.getCell(6));
                String endStationIdStr = dataFormatter.formatCellValue(row.getCell(7));
                String trainIdStr = dataFormatter.formatCellValue(row.getCell(8));

                try {
                    Integer routeId = Integer.parseInt(routeIdStr);
                    LocalTime arrivalTime = LocalTime.parse(arrivalTimeStr, timeFormatter); // Parse time in HH:mm:ss format
                    int dateNumber = Integer.parseInt(dateNumberStr);
                    LocalTime departureTime = LocalTime.parse(departureTimeStr, timeFormatter); // Parse time in HH:mm:ss format
                    Double distance = Double.parseDouble(distanceStr);
                    int stationNumber = Integer.parseInt(stationNumberStr);
                    Integer startStationId = Integer.parseInt(startStationIdStr);
                    Integer endStationId = Integer.parseInt(endStationIdStr);
                    Integer trainId = Integer.parseInt(trainIdStr);

                    // Fetch related StartStation, EndStation, and Train entities
                    Optional<Station> startStation = stationService.getById(startStationId);
                    Optional<Station> endStation = stationService.getById(endStationId);
                    Optional<Train> train = trainService.getById(trainId);

                    if (startStation.isPresent() && endStation.isPresent() && train.isPresent()) {
                        Route route = new Route();
                        route.setRouteId(routeId);
                        route.setStartStation(startStation.get());
                        route.setEndStation(endStation.get());
                        route.setArrivalTime(arrivalTime);
                        route.setDepartureTime(departureTime);
                        route.setDistance(distance);
                        route.setStationNumber(stationNumber);
                        route.setDateNumber(dateNumber);
                        route.setStatus("active");
                        route.setTrain(train.get());

                        routeService.save(route);
                    } else {
                        System.out.println("StartStation, EndStation, or Train not found for Route ID " + routeId + ". Skipping.");
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.out.println("Invalid number or date format: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Route table already has data. No insertion needed.");
        }
    }


    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
