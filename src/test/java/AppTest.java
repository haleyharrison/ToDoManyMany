import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.junit.Assert.*;
import spark.template.velocity.VelocityTemplateEngine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest{

  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Welcome to To Do List");
  }

  @Test
  public void categoriesListRenders(){
    goTo("http://localhost:4567/categories");
    assertThat(pageSource()).contains("Categories list!");
  }

  @Test
  public void categoriesDisplaysList(){
    Category myCategory = new Category("things to do with nachos");
    myCategory.save();
    String catPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(catPath);
    assertThat(pageSource()).contains("things to do with nachos");
  }

  @Test
  public void addTaskListRenders(){
    goTo("http://localhost:4567/tasks");
    assertThat(pageSource()).contains("Task list!");
  }

  @Test
  public void addTaskWorks(){
    Task task = new Task("eat nachos");
    task.save();
    String taskPath = String.format("http://localhost:4567/tasks/%d", task.getId());
    goTo(taskPath);
    assertThat(pageSource()).contains("eat nachos");
  }

  // @Test
  // public void assignTaskToCatWorks(){
  //   Category myCategory = new Category("things to do with nachos");
  //   myCategory.save();
  //   Task myTask = new Task("eat nachos");
  //   myTask.save();
  //   String path = String.format ("http://localhost:4567/categories/%d", myCategory.getId());
  //   goTo(path);
  //   click("option", with("task_id").equalTo("eat nachos"));
  //   assertThat(pageSource()).contains("eat nachos");
  // }





}
