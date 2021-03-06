package ru.darkkeks.telegram.trackyoursheet.sheets

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.SheetsScopes
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
class CredentialsUtil(
        val jsonFactory: JsonFactory,
        val httpTransport: NetHttpTransport
) {
    private val credentialsFile = "credentials.json"
    private val tokensDirectory = "tokens"

    fun getCredential(): Credential {
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, File(credentialsFile).reader())

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            jsonFactory,
            clientSecrets,
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY)
        )
            .setDataStoreFactory(FileDataStoreFactory(File(tokensDirectory)))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }
}
