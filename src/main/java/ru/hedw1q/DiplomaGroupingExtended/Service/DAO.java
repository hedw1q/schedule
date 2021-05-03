package ru.hedw1q.DiplomaGroupingExtended.Service;

import ru.hedw1q.DiplomaGroupingExtended.Entity.*;

import javax.crypto.Mac;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hedw1q
 */
public class DAO {

    private static DAO instance;

    private DAO() {
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/ScheduleDiploma";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "admin";
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public void createProduct(Product product) {
        String SQL = "INSERT INTO public.\"product\"(name,details_id) VALUES(?,?)";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            List<Integer> subList = new ArrayList<>();
            for (int i = 0; i < product.getDetails().size(); i++) {
                subList.add(product.getDetails().get(i).getId());
            }

            preparedStatement.setString(1, product.getName());
            Array array = connection.createArrayOf("integer", subList.toArray());
            preparedStatement.setArray(2, array);
            preparedStatement.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public void createOrder(Order order) {
        String SQL = "INSERT INTO public.\"order\"(name,products) VALUES(?,?)";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            List<Integer> subList = new ArrayList<>();
            for (int i = 0; i < order.getProducts().size(); i++) {
                subList.add(order.getProducts().get(i).getId());
            }

            preparedStatement.setString(1, order.getName());
            Array array = connection.createArrayOf("integer", subList.toArray());
            preparedStatement.setArray(2, array);
            preparedStatement.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public LinkedList<String> getProductNamesList() {
        String SQL = "SELECT name FROM public.\"product\"";
        LinkedList<String> productNames = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                productNames.add(resultSet.getString("name"));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return productNames;
    }

    public LinkedList<String> getOrderList() {
        String SQL = "SELECT name FROM public.\"order\"";
        LinkedList<String> productNames = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                productNames.add(resultSet.getString("name"));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return productNames;
    }

    public LinkedList<Detail> getDetailsByProductId(int product_id) {
        String SQL = "SELECT id FROM public.\"detail\" WHERE product_id=?";
        LinkedList<Detail> details = new LinkedList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, product_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Detail detail = getDetailById(resultSet.getInt("id"));
                details.add(detail);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        return details;
    }

    public void createDetail(Detail detail) {
        String SQL = "INSERT INTO public.\"detail\"(name,procTime,assemtime,id,product_id,route,route_times) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, detail.getName());
            preparedStatement.setInt(2, detail.computeProcTime());
            preparedStatement.setInt(3, detail.getAssemTime());
            preparedStatement.setInt(4, detail.getId());
            preparedStatement.setInt(5, detail.getProduct_id());

            List list1 = new LinkedList();
            List list2 = new LinkedList();

            for (int i = 0; i < detail.getOperations().size(); i++) {
                list1.add(detail.getOperations().get(i).getMachine().getId());
                list2.add(detail.getOperations().get(i).getTime());
            }
            preparedStatement.setArray(6, connection.createArrayOf("integer", list1.toArray()));
            preparedStatement.setArray(7, connection.createArrayOf("integer", list2.toArray()));

            preparedStatement.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteAllDetails() {
        String SQL = "DELETE FROM public.\"detail\";";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.execute();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public Detail getDetailById(int id) {
        Detail detail = null;
        String SQL = "SELECT * FROM public.\"detail\" where id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            detail = new Detail();
            detail.setId(id);
            detail.setProcTime(resultSet.getInt("proctime"));
            detail.setAssemTime(resultSet.getInt("assemtime"));
            detail.setName(resultSet.getString("name"));
            detail.setProduct_id(resultSet.getInt("product_id"));

            Array array = resultSet.getArray("route");
            Integer[] machineRoutesIds = (Integer[]) array.getArray();

            Array array1 = resultSet.getArray("route_times");
            Integer[] routeTimes = (Integer[]) array1.getArray();

            List<Operation> operations = new LinkedList<>();
            for (int i = 0; i < machineRoutesIds.length; i++) {
                operations.add(new Operation(getMachineById(machineRoutesIds[i]), routeTimes[i]));
            }
            detail.setOperations(operations);


        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return detail;
    }

    public Machine getMachineById(int id) {
        Machine machine = null;
        String SQL = "SELECT * FROM public.\"machine\" where id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            machine = new Machine(id);
            machine.setName(resultSet.getString("name"));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return machine;
    }


    public Product getProductByName(String name) {
        Product product = null;
        String SQL = "SELECT * FROM public.\"product\" where name=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(name);
            Array array = resultSet.getArray("details_id");
            Integer[] detail_ids = (Integer[]) array.getArray();
            List<Detail> detailList = new ArrayList<>();
            for (int number : detail_ids) {
                Detail detail = getDetailById(number);
                detailList.add(detail);
            }
            product.setDetails(detailList);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return product;
    }

    public Product getProductById(int id) {
        Product product = null;
        String SQL = "SELECT * FROM public.\"product\" where id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            product = new Product();
            product.setId(id);
            product.setName(resultSet.getString("name"));
            Array array = resultSet.getArray("details_id");
            Integer[] detail_ids = (Integer[]) array.getArray();
            List<Detail> detailList = new ArrayList<>();
            for (int number : detail_ids) {
                Detail detail = getDetailById(number);
                detailList.add(detail);
            }
            product.setDetails(detailList);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return product;
    }

    public Order getOrderByName(String name) {
        Order order = null;
        String SQL = "SELECT * FROM public.\"order\" where name=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            order = new Order();

            order.setId(resultSet.getInt("id"));
            order.setName(name);

            Array array = resultSet.getArray("products");
            Integer[] ids = (Integer[]) array.getArray();
            List<Product> products = new ArrayList<>();
            for (int number : ids) {
                Product product = getProductById(number);
                products.add(product);
            }
            order.setProducts(products);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return order;
    }
}
