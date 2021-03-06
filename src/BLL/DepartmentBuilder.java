package BLL;

import BE.Department;
import BE.User;
import DAL.ResultSetParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentBuilder implements IDepartmentBuilder{

    private final ResultSetParser resultSetParser = new ResultSetParser();

    private List<Department> departments = new ArrayList<>();

    /**
     * Uses the result set to make the departments
     * @param rs the result set
     * @throws SQLException if something went wrong
     */
    @Override
    public void makeDepartment(ResultSet rs) throws SQLException {
        User placeHolderUser = new User("PlaceHolder");

        while (rs.next()) {
            Department newDepartment = new Department(rs.getInt("dptId"),
                    rs.getString("dptName"), placeHolderUser);
            if (departments.stream().noneMatch(o -> o.getId() == newDepartment.getId())) {
                departments.add(newDepartment);
            }
        }
        rs.beforeFirst();
    }

    /**
     * Adds the users from the given result set
     * @param rs the result set
     * @throws SQLException if something went wrong
     */
    @Override
    public void addUsers(ResultSet rs) throws SQLException {

        while (rs.next()) {

            User user = resultSetParser.getUser(rs);
            for (Department d : departments) {
                if (rs.getInt("dptId") == d.getId() &&
                        user.getUserName().equals(rs.getString("Manager"))) {
                    d.setManager(user);
                }
                if (user.getUserName() != null)
                    if (rs.getString("dptName").equals(d.getName()) && d.getUsers().stream().noneMatch(
                            o -> o.getUserName().equals(user.getUserName()))) {
                        d.addUser(user);
                    }
            }
        }
        rs.beforeFirst();
    }

    /**
     * Adds subDepartments to Departments given the result set
     * @param rs the result set
     * @throws SQLException if something went wrong
     */
    @Override
    public void addSubdepartments(ResultSet rs) throws SQLException {
        while (rs.next()) {
            for (Department dpt : departments) {
                if (dpt.getId() == rs.getInt("dptId")) {
                    for (Department subDpt : departments) {
                        if (subDpt.getId() == (rs.getInt("subDpt"))) {
                            if (!dpt.getSubDepartments().contains(subDpt)) {
                                dpt.addSubDepartment(subDpt);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the departments created
     * @return a list of created departments
     */
    public List<Department> getResult(){
        return this.departments;
    }
}
