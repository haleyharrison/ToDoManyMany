import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Category {
  private int id;
  private String description;

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public Category(String description) {
    this.description = description;
  }

  public static List<Category> all() {
    String sql = "SELECT id, description FROM Categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  @Override
  public boolean equals(Object otherCategory){
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getDescription().equals(newCategory.getDescription());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Categories (description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Categories where id=:id";
      Category Category = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
      return Category;
    }
  }

  public void addTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .addParameter("task_id", task.getId())
        .executeUpdate();
    }
  }

  public void dissociateTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM categories_tasks WHERE category_id =:category_id AND task_id =:task_id";
      con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .addParameter("task_id", task.getId())
        .executeUpdate();
    }
  }

  //This doesn't delete a task, it just disassociates a task from a category

  public ArrayList<Task> getTasks() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT task_id FROM categories_tasks WHERE category_id = :category_id";
      List<Integer> taskIds = con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Task> tasks = new ArrayList<Task>();

      for (Integer taskId : taskIds) {
          String taskQuery = "Select * From tasks WHERE id = :taskId";
          Task task = con.createQuery(taskQuery)
            .addParameter("taskId", taskId)
            .executeAndFetchFirst(Task.class);
          tasks.add(task);
      }
      return tasks;
    }
  }


  public void delete() {
  try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM categories WHERE id = :id;";
      con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();

    String joinDeleteQuery = "DELETE FROM categories_tasks WHERE category_id = :categoryId";
      con.createQuery(joinDeleteQuery)
        .addParameter("categoryId", this.getId())
        .executeUpdate();
  }
  }
}
