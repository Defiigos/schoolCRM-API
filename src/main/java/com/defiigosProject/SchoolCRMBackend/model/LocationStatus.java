package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Location> locations = new HashSet<>();

    public LocationStatus(LocationStatusType status) {
        this.status = status;
    }

    public void addLocation(Location location) {
        this.locations.add(location);
        location.setStatus(this);
    }

    public void removeLocation(Location location) {
        this.locations.remove(location);
        location.setStatus(null);
    }
}
