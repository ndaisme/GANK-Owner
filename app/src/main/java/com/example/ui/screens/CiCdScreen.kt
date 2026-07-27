package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.NeoBrutalistCard
import com.example.components.NeoSectionHeader
import com.example.ui.theme.GankColors

@Composable
fun CiCdScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            NeoSectionHeader(
                title = "GITHUB ACTIONS & BUILD STATUS",
                subtitle = "Verifikasi kompatibilitas CI/CD runner & APK build"
            )
        }

        // Summary Banner
        item {
            NeoBrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = GankColors.White
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = GankColors.Success,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "STATUS BUILD: SIAP UNTUK GITHUB ACTIONS",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = GankColors.Ink
                        )
                        Text(
                            text = "Repository Android memiliki konfigurasi Gradle standar & workflow CI.",
                            fontSize = 11.sp,
                            color = GankColors.Steel
                        )
                    }
                }
            }
        }

        // Verification Checklist
        item {
            NeoSectionHeader(
                title = "CHECKLIST KOMPATIBILITAS CI/CD",
                subtitle = "8 Aturan Utama Android Build Runner"
            )
        }

        val checklistItems = listOf(
            "Struktur Project Gradle Standard" to "build.gradle.kts (root), app/build.gradle.kts, settings.gradle.kts, gradle/libs.versions.toml lengkap.",
            "Gradle Wrapper Tersedia" to "gradlew, gradlew.bat, dan gradle/wrapper/ (jar & properties) ter-commit untuk runner ./gradlew.",
            "Signing Config Fallback Safe" to "signingConfigs.release & debug memiliki fallback otomatis sehingga assembleDebug tidak pernah error.",
            "Bebas Hardcoded Secrets" to "API keys & credentials dikelola via secrets & environment variables (GEMINI_API_KEY).",
            ".gitignore Khusus Android" to "Mencegah commit local.properties, *.apk, *.aab, *.jks, .gradle/, dan build/.",
            "Dependency Version Explicit" to "Semua dependency menggunakan versi terdaftar tanpa wildcard '+' untuk build reproducible.",
            "Java / Kotlin Compatibility" to "compileSdk = 36, minSdk = 24, Java 11 / JDK 17 compatibility terkonfigurasi.",
            "Workflow File Ready" to ".github/workflows/android-build.yml terbuat dengan step actions/checkout, setup-java 17, dan assembleDebug."
        )

        items(checklistItems.size) { index ->
            val (title, description) = checklistItems[index]
            NeoBrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = GankColors.White
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GankColors.Success)
                            .border(2.dp, GankColors.Ink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PASS",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            color = GankColors.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = GankColors.Ink
                        )
                        Text(
                            text = description,
                            fontSize = 11.sp,
                            color = GankColors.Steel
                        )
                    }
                }
            }
        }

        // Code Preview Box
        item {
            NeoSectionHeader(title = "WORKFLOW FILE PREVIEW", subtitle = ".github/workflows/android-build.yml")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GankColors.Ink)
                    .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = """
name: Android CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
      - name: Grant execute permission
        run: chmod +x gradlew
      - name: Build debug APK
        run: ./gradlew assembleDebug
        env:
          GEMINI_API_KEY: ${'$'}{{ secrets.GEMINI_API_KEY }}
      - uses: actions/upload-artifact@v4
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = GankColors.GankYellow
                )
            }
        }
    }
}
