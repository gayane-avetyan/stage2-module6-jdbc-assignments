package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "insert into myusers (firstname, lastname, age) values (?, ?, ?)";
    private static final String updateUserSQL = "update myusers set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String deleteUser = "delete from myusers where id = ?";
    private static final String findUserByIdSQL = "select * from myusers where id = ?";
    private static final String findUserByNameSQL = "select * from myusers where firstname = ?";
    private static final String findAllUserSQL = "select * from myusers";


    public Long createUser(User user) throws SQLException {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps.getConnection().prepareStatement(createUserSQL);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();

            if (generatedKeys.next())
                return generatedKeys.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
        return null;
    }

    public User findUserById(Long userId) throws SQLException {

        User user = new User();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps.getConnection().prepareStatement(findUserByIdSQL);

            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge((resultSet.getInt("age")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
        return user;
    }

    public User findUserByName(String userName) throws SQLException {
        User user = new User();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps.getConnection().prepareStatement(findUserByNameSQL);

            ps.setString(1, userName);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge((resultSet.getInt("age")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
        return user;
    }

    public List<User> findAllUser() throws SQLException {

        List<User> users = new ArrayList<>();

        try {
            st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(findAllUserSQL);

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge((resultSet.getInt("age")));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
        return users;
    }

    public User updateUser(User user) throws SQLException {

        User updatedUser = new User();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps.getConnection().prepareStatement(updateUserSQL);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());

            ps.executeUpdate();

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                updatedUser.setFirstName(resultSet.getString("firstname"));
                updatedUser.setLastName(resultSet.getString("lastName"));
                updatedUser.setAge((resultSet.getInt("age")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
        return updatedUser;

    }

    public void deleteUser(Long userId) throws SQLException {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection == null)
                connection.close();

            if (ps == null)
                ps.close();
        }
    }
}
