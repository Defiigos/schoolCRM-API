package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location_statuses")
@Getter
@Setter
@NoArgsConstructor
public class LocationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private LocationStatusType status;

    @OneToMany(mappedBy = "locationStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locationList = new ArrayList<>();

    public LocationStatus(LocationStatusType status) {
        this.status = status;
    }

    public void addLocation(Location location) {
        this.locationList.add(location);
        location.setLocationStatus(this);
    }

    public void removeLocation(Location location) {
        this.locationList.remove(location);
        location.setLocationStatus(null);
    }
}
