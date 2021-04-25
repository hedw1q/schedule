package ru.hedw1q.DiplomaGroupingExtended.Service;

import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hedw1q
 */
public class DAO {

    private static DAO instance;

    private DAO() {
    }

    public static DAO getInstance(){
        if (instance==null) {
            instance=new DAO();
        }
        return instance;
    }

    private static final String URL="jdbc:postgresql://localhost:5432/ScheduleDiploma";
    private static final String LOGIN="postgres";
    private static final String PASSWORD="admin" ;
    private static Connection connection;

    static {
        try {
            connection= DriverManager.getConnection(URL,LOGIN,PASSWORD);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public void createProduct(Product product){
        String SQL="INSERT INTO public.\"product\"(name,details_id) VALUES(?,?)";
        try {

            PreparedStatement preparedStatement=connection.prepareStatement(SQL);
            List<Integer> subList=new ArrayList<>();
            for(int i=0;i<product.getDetails().size();i++){
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

    public List<String> getProductNamesList(){
        String SQL="SELECT name FROM public.\"product\"";
        List <String> productNames=new ArrayList<>();
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery(SQL);
            while(resultSet.next()){
                productNames.add(resultSet.getString("name"));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return productNames;
    }

    public void createDetail(Detail detail){
        String SQL="INSERT INTO public.\"detail\"(name,proctime,assemtime,id,product_id) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(SQL);
            preparedStatement.setString(1, detail.getName());
            preparedStatement.setInt(2,detail.getProcTime());
            preparedStatement.setInt(3,detail.getAssemTime());
            preparedStatement.setInt(4,detail.getId());
            preparedStatement.setInt(5,detail.getProduct_id());
            preparedStatement.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
public void deleteAllDetails(){
    String SQL="DELETE FROM public.\"detail\";";
    try {
        PreparedStatement preparedStatement=connection.prepareStatement(SQL);
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
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return detail;
    }

    public Product getProductByName(String name){
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
            Array array=resultSet.getArray("details_id");
            Integer[] detail_ids=(Integer[])array.getArray();
            List<Detail> detailList=new ArrayList<>();
            for(int number:detail_ids){
                Detail detail=getDetailById(number);
                detailList.add(detail);
            }
            product.setDetails(detailList);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return product;
    }

}
