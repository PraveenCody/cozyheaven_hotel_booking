package com.springboot.cozyheaven.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize

                        // login token generation
                        .requestMatchers(HttpMethod.GET,"/auth/login").authenticated()
                        // user details API
                        .requestMatchers(HttpMethod.GET,"/auth/user-details").authenticated()

                        // ADMIN
                        .requestMatchers(HttpMethod.POST,"/admin/add/").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/admin/update/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/admin/getAll/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/admin/delete/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/admin/").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/hotelOwner/getAll/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/hotel/getAll/").hasAnyAuthority("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/bookings/all/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/customer/getAll/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/payment/getAll/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/review/all/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/bookings/chart/admin/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/payment/chart/admin/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/hotel/chart/admin/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/room/chart/admin/").hasAuthority("ADMIN")

                        //Amenity
                        .requestMatchers(HttpMethod.POST,"/amenity/add/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/amenity/update/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/amenity/delete/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/amenity/getById/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/amenity/getAll/").hasAuthority("ADMIN")

                        //COUPON
                        .requestMatchers(HttpMethod.POST,"/coupon/add/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/coupon/get/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/coupon/getAll/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/coupon/update/").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/coupon/delete/{id}").hasAuthority("ADMIN")


                        // HOTEL OWNER
                        .requestMatchers(HttpMethod.POST,"/hotelOwner/add/").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/hotelOwner/delete/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/hotelOwner/update/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/booking/getByOwner/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/hotelOwner/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/bookings/chart/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/payment/chart/").hasAuthority("HOTEL_OWNER")



                        //HOTEL
                        .requestMatchers(HttpMethod.POST,"/hotel/add/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/hotel/get/{id}").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/hotel/getAllByHotelOwner/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.PUT,"/hotel/update/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.DELETE,"/hotel/delete/{id}").hasAnyAuthority("HOTEL_OWNER","ADMIN")

                        //ROOM
                        .requestMatchers(HttpMethod.POST,"/room/add/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.PUT,"/room/update/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/room/getRoom/{id}").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.DELETE,"/room/delete/{id}").hasAnyAuthority("HOTEL_OWNER","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/room/getAllByOwner/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/room/getAllByHotel/").hasAuthority("HOTEL_OWNER")

                        //HOTEL AMENITY
                        .requestMatchers(HttpMethod.POST,"/hotelAmenity/addingAmenity/{aid}/Hotel/{hid}").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.POST,"/hotelAmenity/delete/{id}").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/review/all/hotel/{hid}").hasAuthority("HOTEL_OWNER")

                        // CUSTOMER
                        .requestMatchers(HttpMethod.POST,"/customer/add/").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/customer/delete/{id}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT,"/customer/update/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/cus/hotel/{id}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/customer/").hasAuthority("CUSTOMER")

                        .requestMatchers(HttpMethod.GET,"/room/dynamicSearch/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/room/getAllRooms/").hasAnyAuthority("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/room/seeRoom/{id}").hasAuthority("CUSTOMER")

                        //BOOKINGS
                        .requestMatchers(HttpMethod.POST,"/booking/add/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/booking/getById/{id}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/bookings/MyBookings/").hasAuthority("CUSTOMER")


                        //PAYMENT
                        .requestMatchers(HttpMethod.POST,"/payment/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/payment/getById/{bid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/payment/myPayments/").hasAuthority("CUSTOMER")


                        //CANCELLATION
                        .requestMatchers(HttpMethod.POST,"/cancellation/{bid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/cancellation/all/").hasAuthority("CUSTOMER")

                        //WISHLIST
                        .requestMatchers(HttpMethod.POST,"/addWishlist/cus{cid}/hotel{hid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE,"/wishlist/delete/{id}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/MyWishlist/").hasAuthority("CUSTOMER")


                        //REVIEW
                        .requestMatchers(HttpMethod.POST,"/review/add/{pid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT,"/review/edit/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/review/all/customer/").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/review/all/hotel/").hasAuthority("HOTEL_OWNER")
                        .requestMatchers(HttpMethod.GET,"/review/all/").hasAuthority("ADMIN")

                        .anyRequest().permitAll()
                );

        http.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());
        return http.build();

    }

    @Bean
    public AuthenticationProvider getProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(getEncoded());
        return daoAuthenticationProvider;

    }

    @Bean
    public PasswordEncoder getEncoded(){
        return new BCryptPasswordEncoder();
    }
}
