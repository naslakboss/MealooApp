package codebuddies.MealooApp.entities;




import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meal", fetch = FetchType.EAGER)
    private Set<Product> products;

    private int price;

    private String mealDifficulty;


    public Meal() {
    }

//    public Meal(String name, Set<Product> products, int price, String mealDifficulty) {
//        this.name = name;
//        this.products = products;
//        this.price = price;
//        this.mealDifficulty = mealDifficulty;
//    }

    public Meal(long id, String name, Set<Product> products, int price, String mealDifficulty) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.price = price;
        this.mealDifficulty = mealDifficulty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMealDifficulty() {
        return mealDifficulty;
    }

    public void setMealDifficulty(String mealDifficulty) {
        this.mealDifficulty = mealDifficulty;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", products=" + products +
                ", price=" + price +
                ", mealDifficulty=" + mealDifficulty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return price == meal.price &&
                Objects.equals(name, meal.name) &&
                Objects.equals(products, meal.products) &&
                Objects.equals(mealDifficulty, meal.mealDifficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, products, price, mealDifficulty);
    }
}
