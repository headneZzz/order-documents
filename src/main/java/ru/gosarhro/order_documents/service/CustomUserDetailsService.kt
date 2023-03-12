package ru.gosarhro.order_documents.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.config.AppConfig
import java.io.File

@Service
class CustomUserDetailsService(
    private val appConfig: AppConfig,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    private fun readerFolderIsNotExists(reader: String): Boolean {
        val readerFolder = File(appConfig.readingRoomPath + File.separator + reader)
        return !readerFolder.exists()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        if (readerFolderIsNotExists(username)) {
            throw UsernameNotFoundException("User not found with username: " + username)
        }
        return User(username, passwordEncoder.encode("1"), arrayListOf())
    }
}
