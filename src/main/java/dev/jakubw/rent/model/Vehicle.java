package dev.jakubw.rent.model;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(nullable = false, unique = true)
    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private String plate;
    @Column(columnDefinition = "NUMERIC")
    private double price;
    private boolean active;


    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String ,Object> attributes = new HashMap<>();

    public Object getAttribute(String key){
        return attributes.get(key);
    }
    public void addAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        if (attributes != null) {
            attributes.remove(key);
        }
    }

    public String printVehicle(){
        String output = "Id: "+id+"\nCategory: "+category+"\nBrand: "+brand+"\nModel: "+model+"\nYear: "+year+"\nPlate: "+plate+"\n";
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            output += entry.getKey() + ": " + entry.getValue().toString() + "\n";
        }
        return output;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object object){
        if (object == this) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vehicle that = (Vehicle) object;
        if (that.brand != this.brand) return false;
        if (that.model != this.model) return false;
        if (that.year != this.year) return false;
        if (that.id != this.id) return false;
        return true;
    }
}
