package co.jht.model.domain.persist.appuser;

import co.jht.enums.UserRole;
import co.jht.enums.UserStatus;
import co.jht.serializer.ZonedDateTimeDeserializer;
import co.jht.serializer.ZonedDateTimeSerializer;
import co.jht.util.DateTimeFormatterUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.ZonedDateTime;
import java.util.Objects;

import static co.jht.enums.UserRole.ROLE_USER;
import static co.jht.enums.UserStatus.ACTIVE;

@Entity
@Table(name = "app_user_table")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("username")
    @Column(nullable = false, unique = true)
    private String username;

    @JsonProperty("password")
    @Column(nullable = false)
    private String password;

    @JsonProperty("email")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty("first_name")
    @Column(nullable = false)
    private String firstName;

    @JsonProperty("last_name")
    @Column(nullable = false)
    private String lastName;

    @JsonProperty("profile_picture_url")
    @Column(nullable = false)
    private String profilePictureUrl = "http://default.jpg";

    @JsonProperty("registration_date")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime registrationDate;

    @JsonProperty("account_status")
    @Column(nullable = false)
    private UserStatus accountStatus = ACTIVE;

    @JsonProperty("role")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role = ROLE_USER;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    public void prePersist() {
        this.registrationDate = Objects.requireNonNullElseGet(
                this.registrationDate,
                DateTimeFormatterUtil::getCurrentTokyoTime
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public ZonedDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(UserStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}