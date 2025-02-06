package co.jht.model.domain.persist.entity.appuser;

import co.jht.enums.UserRole;
import co.jht.enums.UserStatus;
import co.jht.serializer.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;
import static co.jht.enums.UserStatus.ACTIVE;
import static co.jht.util.DateTimeFormatterUtil.getFormatter;

@Entity
@Table(name = "app_user_table")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("user_name")
    @Column(nullable = false, unique = true)
    private String userName;

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
    private String profilePictureUrl;

    @JsonProperty("registration_date")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime registrationDate;

    @JsonProperty("account_status")
    @Column(nullable = false)
    private UserStatus accountStatus = ACTIVE;

    @JsonProperty("role")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @PrePersist
    public void prePersist() {
        this.registrationDate =
                ZonedDateTime.parse(
                    Objects.requireNonNullElseGet(
                        this.registrationDate,
                        () -> ZonedDateTime.now(ZoneId.of(ASIA_TOKYO))
                    ).format(getFormatter())
                );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}