package products;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class Category {
    Integer id_category;
    String category;

    public Category(Integer id_category, String category) {
        this.id_category = id_category;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category1)) return false;
        return Objects.equals(id_category, category1.id_category) && Objects.equals(category, category1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_category, category);
    }

    @Override
    public String toString() {
        return
                "id: " + id_category + " - " +
                        category;
    }
}
