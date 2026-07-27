package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainTab
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.GankColors
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
                val services by viewModel.services.collectAsStateWithLifecycle()
                val spareparts by viewModel.spareparts.collectAsStateWithLifecycle()
                val transactions by viewModel.transactions.collectAsStateWithLifecycle()
                val customers by viewModel.customers.collectAsStateWithLifecycle()
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val selectedFilter by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()

                val totalIncome = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }

                var showQuickAddServiceDialog by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        GankTopAppBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    },
                    bottomBar = {
                        GankBottomNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    },
                    floatingActionButton = {
                        if (selectedTab == MainTab.DASHBOARD || selectedTab == MainTab.SERVICES) {
                            FloatingActionButton(
                                onClick = {
                                    viewModel.selectTab(MainTab.SERVICES)
                                    showQuickAddServiceDialog = true
                                },
                                containerColor = GankColors.GankYellow,
                                contentColor = GankColors.Ink,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .border(3.dp, GankColors.Ink, RoundedCornerShape(10.dp))
                                    .testTag("fab_add_service")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Tambah Servis",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    },
                    containerColor = GankColors.Paper
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            MainTab.DASHBOARD -> DashboardScreen(
                                services = services,
                                spareparts = spareparts,
                                transactions = transactions,
                                totalIncome = totalIncome,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onAddServiceClick = {
                                    viewModel.selectTab(MainTab.SERVICES)
                                    showQuickAddServiceDialog = true
                                }
                            )

                            MainTab.SERVICES -> ServiceListScreen(
                                services = services,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                selectedFilter = selectedFilter,
                                onSelectFilter = { viewModel.setStatusFilter(it) },
                                onAdvanceStatus = { viewModel.advanceServiceStatus(it) },
                                onAddService = { name, phone, device, imei, complaint, cost, dp, capitalCost, createdAt ->
                                    viewModel.addService(name, phone, device, imei, complaint, cost, dp, capitalCost, createdAt)
                                    showQuickAddServiceDialog = false
                                },
                                showAddDialogInitially = showQuickAddServiceDialog,
                                onDismissAddDialog = { showQuickAddServiceDialog = false }
                            )

                            MainTab.SPAREPARTS -> SparepartScreen(
                                spareparts = spareparts,
                                onAddSparepart = { barcode, name, category, stock, buy, sell, rack ->
                                    viewModel.addSparepart(barcode, name, category, stock, buy, sell, rack)
                                },
                                onUpdateStock = { id, change ->
                                    viewModel.updateSparepartStock(id, change)
                                }
                            )

                            MainTab.FINANCE -> FinanceScreen(
                                transactions = transactions,
                                onAddTransaction = { type, category, amount, desc ->
                                    viewModel.addTransaction(type, category, amount, desc)
                                }
                            )

                            MainTab.CUSTOMERS -> CustomerScreen(
                                customers = customers,
                                onAddCustomer = { name, phone, address ->
                                    viewModel.addCustomer(name, phone, address)
                                }
                            )

                            MainTab.SETTINGS -> SettingsScreen(
                                onClearDummyData = { viewModel.clearAllDummyData() },
                                onResetSampleData = { viewModel.resetSampleData() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GankTopAppBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GankColors.Ink)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(GankColors.GankYellow)
                        .border(2.dp, GankColors.White, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", fontWeight = FontWeight.Black, fontSize = 18.sp, color = GankColors.Ink)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "GANK SERVICE",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = GankColors.White
                    )
                    Text(
                        text = selectedTab.title.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GankColors.GankYellow
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GankColors.NeonBlue)
                    .clickable { onTabSelected(MainTab.SETTINGS) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "CI Status",
                        tint = GankColors.Ink,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "CI/CD OK",
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp,
                        color = GankColors.Ink
                    )
                }
            }
        }
    }
}

@Composable
fun GankBottomNavBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GankColors.Ink)
            .navigationBarsPadding()
            .border(width = 3.dp, color = GankColors.Ink)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                title = "Home",
                icon = Icons.Default.Home,
                isSelected = selectedTab == MainTab.DASHBOARD,
                onClick = { onTabSelected(MainTab.DASHBOARD) },
                testTag = "tab_dashboard"
            )

            NavItem(
                title = "Servis",
                icon = Icons.Default.Build,
                isSelected = selectedTab == MainTab.SERVICES,
                onClick = { onTabSelected(MainTab.SERVICES) },
                testTag = "tab_services"
            )

            NavItem(
                title = "Stok",
                icon = Icons.Default.ShoppingCart,
                isSelected = selectedTab == MainTab.SPAREPARTS,
                onClick = { onTabSelected(MainTab.SPAREPARTS) },
                testTag = "tab_spareparts"
            )

            NavItem(
                title = "Kas",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = selectedTab == MainTab.FINANCE,
                onClick = { onTabSelected(MainTab.FINANCE) },
                testTag = "tab_finance"
            )

            NavItem(
                title = "User",
                icon = Icons.Default.Person,
                isSelected = selectedTab == MainTab.CUSTOMERS,
                onClick = { onTabSelected(MainTab.CUSTOMERS) },
                testTag = "tab_customers"
            )

            NavItem(
                title = "Setting",
                icon = Icons.Default.Settings,
                isSelected = selectedTab == MainTab.SETTINGS,
                onClick = { onTabSelected(MainTab.SETTINGS) },
                testTag = "tab_settings"
            )
        }
    }
}

@Composable
fun NavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) GankColors.GankYellow else GankColors.Ink)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) GankColors.Ink else GankColors.Ink,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) GankColors.Ink else GankColors.Silver,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isSelected) GankColors.Ink else GankColors.Silver
            )
        }
    }
}
