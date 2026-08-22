package com.example

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.aspectRatio

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

val AppVersion = "v2.1.2"


val LocalLanguage = compositionLocalOf { "English" }

val translations = mapOf(
    "English" to mapOf(
        "Household Sync" to "Household Sync",
        "Dashboard" to "Dashboard",
        "Tasks" to "Tasks",
        "Expenses" to "Expenses",
        "Settings" to "Settings",
        "Calculator" to "Calculator",
        "Room Code" to "Room Code",
        "Good morning" to "Good morning",
        "Good afternoon" to "Good afternoon",
        "Good evening" to "Good evening",
        "Good night" to "Good night",
        "House" to "House",
        "Household Overview" to "Household Overview",
        "Active Tasks" to "Active Tasks",
        "Pending" to "Pending",
        "Spending Trends (6 Months)" to "Spending Trends (6 Months)",
        "New Task" to "New Task",
        "Add" to "Add",
        "Added by:" to "Added by:",
        "Assigned to:" to "Assigned to:",
        "Expense Splitter" to "Expense Splitter",
        "Item Name" to "Item Name",
        "Cost" to "Cost",
        "Who Paid?" to "Who Paid?",
        "Split among:" to "Split among:",
        "Add Expense" to "Add Expense",
        "Balances" to "Balances",
        "owes" to "owes",
        "Settle Up" to "Settle Up",
        "Total Spending by Person" to "Total Spending by Person",
        "Transaction History" to "Transaction History",
        "Settlement" to "Settlement",
        "Paid by:" to "Paid by:",
        "Split among" to "Split among",
        "Role: Admin" to "Role: Admin",
        "Role: Member" to "Role: Member",
        "Admin Controls" to "Admin Controls",
        "Household Name" to "Household Name",
        "Members" to "Members",
        "Dark Mode" to "Dark Mode",
        "Preferences" to "Preferences",
        "Language" to "Language",
        "Currency" to "Currency",
        "Free tier    [Activated]  " to "Free tier    [Activated]  ",
        "Firebase Cloud Tier" to "Firebase Cloud Tier",
        "About" to "About",
        "Developer:" to "Developer:",
        "Version:" to "Version:",
        "My Household" to "My Household",
        "Code copied" to "Code copied"
    ),
    "Hindi" to mapOf(
        "Household Sync" to "हाउसहोल्ड सिंक",
        "Dashboard" to "डैशबोर्ड",
        "Tasks" to "कार्य",
        "Expenses" to "खर्च",
        "Settings" to "सेटिंग्स",
        "Calculator" to "कैलकुलेटर",
        "Room Code" to "रूम कोड",
        "Good morning" to "सुप्रभात",
        "Good afternoon" to "शुभ दोपहर",
        "Good evening" to "शुभ संध्या",
        "Good night" to "शुभ रात्रि",
        "House" to "घर",
        "Household Overview" to "हाउसहोल्ड अवलोकन",
        "Active Tasks" to "सक्रिय कार्य",
        "Pending" to "लंबित",
        "Spending Trends (6 Months)" to "खर्च के रुझान (6 महीने)",
        "New Task" to "नया कार्य",
        "Add" to "जोड़ें",
        "Added by:" to "द्वारा जोड़ा गया:",
        "Assigned to:" to "को सौंपा गया:",
        "Expense Splitter" to "खर्च विभाजक",
        "Item Name" to "आइटम का नाम",
        "Cost" to "लागत",
        "Who Paid?" to "किसने भुगतान किया?",
        "Split among:" to "में विभाजित करें:",
        "Add Expense" to "खर्च जोड़ें",
        "Balances" to "बकाया",
        "owes" to "देना है",
        "Settle Up" to "निपटान करें",
        "Total Spending by Person" to "व्यक्ति द्वारा कुल खर्च",
        "Transaction History" to "लेनदेन इतिहास",
        "Settlement" to "निपटान",
        "Paid by:" to "भुगतानकर्ता:",
        "Split among" to "में विभाजित",
        "Role: Admin" to "भूमिका: एडमिन",
        "Role: Member" to "भूमिका: सदस्य",
        "Admin Controls" to "एडमिन नियंत्रण",
        "Household Name" to "हाउसहोल्ड का नाम",
        "Members" to "सदस्य",
        "Dark Mode" to "डार्क मोड",
        "Preferences" to "प्राथमिकताएं",
        "Language" to "भाषा",
        "Currency" to "मुद्रा",
        "Free tier    [Activated]  " to "सदस्यता",
        "Firebase Cloud Tier" to "फ़ायरबेस क्लाउड टियर",
        "About" to "के बारे में",
        "Developer:" to "डेवलपर:",
        "Version:" to "संस्करण:",
        "My Household" to "मेरा घर",
        "Code copied" to "कोड कॉपी किया गया"
    ),
    "Bengali" to mapOf(
        "Household Sync" to "হাউসহোল্ড সিঙ্ক",
        "Dashboard" to "ড্যাশবোর্ড",
        "Tasks" to "কাজ",
        "Expenses" to "খরচ",
        "Settings" to "সেটিংস",
        "Calculator" to "ক্যালকুলেটর",
        "Room Code" to "রুম কোড",
        "Good morning" to "সুপ্রভাত",
        "Good afternoon" to "শুভ বিকাল",
        "Good evening" to "শুভ সন্ধ্যা",
        "Good night" to "শুভ রাত্রি",
        "House" to "বাড়ি",
        "Household Overview" to "হাউসহোল্ড ওভারভিউ",
        "Active Tasks" to "সক্রিয় কাজ",
        "Pending" to "বাকি",
        "Spending Trends (6 Months)" to "খরচের প্রবণতা (৬ মাস)",
        "New Task" to "নতুন কাজ",
        "Add" to "যোগ করুন",
        "Added by:" to "যোগ করেছেন:",
        "Assigned to:" to "বরাদ্দ করা হয়েছে:",
        "Expense Splitter" to "খরচ বিভাজক",
        "Item Name" to "আইটেমের নাম",
        "Cost" to "খরচ",
        "Who Paid?" to "কে পেমেন্ট করেছে?",
        "Split among:" to "যাদের মধ্যে ভাগ হবে:",
        "Add Expense" to "খরচ যোগ করুন",
        "Balances" to "ব্যালেন্স",
        "owes" to "দেনা",
        "Settle Up" to "নিষ্পত্তি করুন",
        "Total Spending by Person" to "ব্যক্তি দ্বারা মোট খরচ",
        "Transaction History" to "লেনদেনের ইতিহাস",
        "Settlement" to "নিষ্পত্তি",
        "Paid by:" to "পেমেন্ট করেছে:",
        "Split among" to "যাদের মধ্যে ভাগ হয়েছে",
        "Role: Admin" to "ভূমিকা: অ্যাডমিন",
        "Role: Member" to "ভূমিকা: সদস্য",
        "Admin Controls" to "অ্যাডমিন নিয়ন্ত্রণ",
        "Household Name" to "হাউসহোল্ডের নাম",
        "Members" to "সদস্যরা",
        "Dark Mode" to "ডার্ক মোড",
        "Preferences" to "পছন্দসমূহ",
        "Language" to "ভাষা",
        "Currency" to "মুদ্রা",
        "Free tier    [Activated]  " to "সাবস্ক্রিপশন",
        "Firebase Cloud Tier" to "ফায়ারবেস ক্লাউড টিয়ার",
        "About" to "সম্পর্কে",
        "Developer:" to "ডেভেলপার:",
        "Version:" to "সংস্করণ:",
        "My Household" to "আমার বাড়ি",
        "Code copied" to "কোড কপি করা হয়েছে"
    )
)

@Composable
fun getString(key: String): String {
    val lang = LocalLanguage.current
    return translations[lang]?.get(key) ?: translations["English"]?.get(key) ?: key
}


val BentoLightBg = Color.White
val BentoLightTextMain = Color(0xFF1B1B1F)
val BentoLightTextSub = Color(0xFF44474E)
val BentoLightBlueBg = Color(0xFFD3E3FD)
val BentoLightBlueText = Color(0xFF001D35)
val BentoLightPurpleBg = Color(0xFFE8DEF8)
val BentoLightPurpleText = Color(0xFF21005D)
val BentoLightBorder = Color(0xFFE1E2E9)
val BentoLightAccentBlue = Color(0xFF0061A4)
val BentoLightGrayBg = Color(0xFFF1F0F4)
val BentoLightCardBg = Color.White

val BentoDarkBg = Color.Black
val BentoDarkTextMain = Color(0xFFE3E2E6)
val BentoDarkTextSub = Color(0xFFC4C6D0)
val BentoDarkBlueBg = Color(0xFF004A77)
val BentoDarkBlueText = Color(0xFFC2E7FF)
val BentoDarkPurpleBg = Color(0xFF4A3B69)
val BentoDarkPurpleText = Color(0xFFE8DEF8)
val BentoDarkBorder = Color(0xFF44474E)
val BentoDarkAccentBlue = Color(0xFF7CB4F9)
val BentoDarkGrayBg = Color(0xFF2D2F33)
val BentoDarkCardBg = Color(0xFF1B1B1F)

@Composable fun bentoBg(isDark: Boolean) = if (isDark) BentoDarkBg else BentoLightBg
@Composable fun bentoTextMain(isDark: Boolean) = if (isDark) BentoDarkTextMain else BentoLightTextMain
@Composable fun bentoTextSub(isDark: Boolean) = if (isDark) BentoDarkTextSub else BentoLightTextSub
@Composable fun bentoBlueBg(isDark: Boolean) = if (isDark) BentoDarkBlueBg else BentoLightBlueBg
@Composable fun bentoBlueText(isDark: Boolean) = if (isDark) BentoDarkBlueText else BentoLightBlueText
@Composable fun bentoPurpleBg(isDark: Boolean) = if (isDark) BentoDarkPurpleBg else BentoLightPurpleBg
@Composable fun bentoPurpleText(isDark: Boolean) = if (isDark) BentoDarkPurpleText else BentoLightPurpleText
@Composable fun bentoBorder(isDark: Boolean) = if (isDark) BentoDarkBorder else BentoLightBorder
@Composable fun bentoAccentBlue(isDark: Boolean) = if (isDark) BentoDarkAccentBlue else BentoLightAccentBlue
@Composable fun bentoGrayBg(isDark: Boolean) = if (isDark) BentoDarkGrayBg else BentoLightGrayBg
@Composable fun bentoCardBg(isDark: Boolean) = if (isDark) BentoDarkCardBg else BentoLightCardBg

val BentoCardShape = RoundedCornerShape(28.dp)
val BentoInnerShape = RoundedCornerShape(16.dp)

data class GroceryItem(val id: String = "", val name: String = "", @get:com.google.firebase.firestore.PropertyName("isChecked") @set:com.google.firebase.firestore.PropertyName("isChecked") var isChecked: Boolean = false, val taskGiver: String = "", val completedAt: Long = 0L)

enum class Priority(val color: Color, val label: String) {
    High(Color(0xFFFF5252), "High"),
    Medium(Color(0xFFFFD740), "Med"),
    Low(Color(0xFF69F0AE), "Low")
}

data class Chore(val id: String = "", val name: String = "", val priority: String = Priority.Low.name, @get:com.google.firebase.firestore.PropertyName("isChecked") @set:com.google.firebase.firestore.PropertyName("isChecked") var isChecked: Boolean = false, val assignedTo: String = "", val taskGiver: String = "", val completedAt: Long = 0L)

data class ExpenseItem(val id: String = "", val name: String = "", val cost: Double = 0.0, val paidBy: String = "", val type: String = "EXPENSE", val paidTo: String = "", val splitAmong: List<String> = emptyList(), val timestamp: Long = 0L)

data class HouseholdData(val code: String = "", val name: String = "", val createdBy: String = "", val members: List<String> = emptyList())

class HouseholdRepository(private val householdId: String, private val onError: (String) -> Unit) {
    private val db = FirebaseFirestore.getInstance()
    private val householdRef = db.collection("households").document(householdId)

    fun getHousehold(onUpdate: (HouseholdData?) -> Unit) {
        householdRef.addSnapshotListener { snapshot, e ->
            if (e != null) { onError(e.message ?: "Error fetching household"); return@addSnapshotListener }
            val data = snapshot?.toObject(HouseholdData::class.java)
            onUpdate(data)
        }
    }
    
    fun updateHouseholdName(newName: String) {
        householdRef.update("name", newName).addOnFailureListener { onError(it.message ?: "Failed to update name") }
    }
    
    fun removeMember(memberEmail: String, currentMembers: List<String>) {
        householdRef.update("members", currentMembers - memberEmail).addOnFailureListener { onError(it.message ?: "Failed to remove member") }
    }

    

    fun getGroceries(onUpdate: (List<GroceryItem>) -> Unit) {
        householdRef.collection("groceries").addSnapshotListener { snapshot, e ->
            if (e != null) { onError(e.message ?: "Error fetching groceries"); return@addSnapshotListener }
            val items = snapshot?.documents?.mapNotNull { it.toObject(GroceryItem::class.java) } ?: emptyList()
            val now = System.currentTimeMillis()
            val validItems = items.filter { !it.isChecked || it.completedAt == 0L || (now - it.completedAt) < 24L * 60 * 60 * 1000 }
            items.filter { it.isChecked && it.completedAt > 0L && (now - it.completedAt) >= 24L * 60 * 60 * 1000 }.forEach { deleteGrocery(it.id) }
            onUpdate(validItems)
        }
    }

    fun addGrocery(item: GroceryItem) {
        val id = UUID.randomUUID().toString()
        householdRef.collection("groceries").document(id).set(item.copy(id = id)).addOnFailureListener { onError(it.message ?: "Failed to add") }
    }

    fun updateGrocery(item: GroceryItem) {
        householdRef.collection("groceries").document(item.id).set(item).addOnFailureListener { onError(it.message ?: "Failed to update") }
    }

    fun deleteGrocery(id: String) {
        householdRef.collection("groceries").document(id).delete().addOnFailureListener { onError(it.message ?: "Failed to delete") }
    }

    fun getChores(onUpdate: (List<Chore>) -> Unit) {
        householdRef.collection("chores").addSnapshotListener { snapshot, e ->
            if (e != null) { onError(e.message ?: "Error fetching chores"); return@addSnapshotListener }
            val items = snapshot?.documents?.mapNotNull { it.toObject(Chore::class.java) } ?: emptyList()
            val now = System.currentTimeMillis()
            val validItems = items.filter { !it.isChecked || it.completedAt == 0L || (now - it.completedAt) < 24L * 60 * 60 * 1000 }
            items.filter { it.isChecked && it.completedAt > 0L && (now - it.completedAt) >= 24L * 60 * 60 * 1000 }.forEach { deleteChore(it.id) }
            onUpdate(validItems)
        }
    }

    fun addChore(chore: Chore) {
        val id = UUID.randomUUID().toString()
        householdRef.collection("chores").document(id).set(chore.copy(id = id)).addOnFailureListener { onError(it.message ?: "Failed to add") }
    }

    fun updateChore(chore: Chore) {
        householdRef.collection("chores").document(chore.id).set(chore).addOnFailureListener { onError(it.message ?: "Failed to update") }
    }

    fun deleteChore(id: String) {
        householdRef.collection("chores").document(id).delete().addOnFailureListener { onError(it.message ?: "Failed to delete") }
    }

    fun getExpenses(onUpdate: (List<ExpenseItem>) -> Unit) {
        householdRef.collection("expenses").addSnapshotListener { snapshot, e ->
            if (e != null) { onError(e.message ?: "Error fetching expenses"); return@addSnapshotListener }
            val items = snapshot?.documents?.mapNotNull { it.toObject(ExpenseItem::class.java) } ?: emptyList()
            onUpdate(items)
        }
    }

    fun addExpense(expense: ExpenseItem) {
        val id = UUID.randomUUID().toString()
        householdRef.collection("expenses").document(id).set(expense.copy(id = id)).addOnFailureListener { onError(it.message ?: "Failed to add") }
    }
}

class HouseholdViewModel : ViewModel() {
    private var repository: HouseholdRepository? = null

    private val _household = MutableStateFlow<HouseholdData?>(null)
    val household: StateFlow<HouseholdData?> = _household
    
    private val _groceries = MutableStateFlow<List<GroceryItem>>(emptyList())
    val groceries: StateFlow<List<GroceryItem>> = _groceries
    private val _isLoadingGroceries = MutableStateFlow(true)
    val isLoadingGroceries: StateFlow<Boolean> = _isLoadingGroceries

    private val _chores = MutableStateFlow<List<Chore>>(emptyList())
    val chores: StateFlow<List<Chore>> = _chores
    private val _isLoadingChores = MutableStateFlow(true)
    val isLoadingChores: StateFlow<Boolean> = _isLoadingChores

    private val _expenses = MutableStateFlow<List<ExpenseItem>>(emptyList())
    val expenses: StateFlow<List<ExpenseItem>> = _expenses
    private val _isLoadingExpenses = MutableStateFlow(true)
    val isLoadingExpenses: StateFlow<Boolean> = _isLoadingExpenses

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun clearError() { _error.value = null }


    private var isFirstChoreLoad = true
    private var isFirstExpenseLoad = true
    private var knownChoreIds = setOf<String>()
    private var knownExpenseIds = setOf<String>()
    private var notifyCallback: ((String, String) -> Unit)? = null

    fun initialize(householdId: String, onNotify: (String, String) -> Unit) {
        notifyCallback = onNotify
        repository = HouseholdRepository(householdId) { _error.value = it }
        repository?.getHousehold { _household.value = it }
        repository?.getGroceries { _groceries.value = it; _isLoadingGroceries.value = false }
        repository?.getChores { newChores -> 
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
            if (!isFirstChoreLoad) {
                newChores.forEach { chore ->
                    if (!knownChoreIds.contains(chore.id) && chore.assignedTo == currentUserEmail && chore.taskGiver != currentUserEmail) {
                        notifyCallback?.invoke("New Task Assigned", "You have been assigned: ${chore.name}")
                    }
                }
            }
            knownChoreIds = newChores.map { it.id }.toSet()
            isFirstChoreLoad = false
            _chores.value = newChores; _isLoadingChores.value = false 
        }
        repository?.getExpenses { newExpenses -> 
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
            if (!isFirstExpenseLoad) {
                newExpenses.forEach { exp ->
                    if (!knownExpenseIds.contains(exp.id) && exp.type == "EXPENSE" && exp.paidBy != currentUserEmail) {
                        val isSplitWithMe = exp.splitAmong.contains(currentUserEmail)
                        if (isSplitWithMe || exp.splitAmong.isEmpty()) {
                            notifyCallback?.invoke("New Shared Expense", "${exp.paidBy.substringBefore("@")} added an expense: ${exp.name}")
                        }
                    }
                }
            }
            knownExpenseIds = newExpenses.map { it.id }.toSet()
            isFirstExpenseLoad = false
            _expenses.value = newExpenses; _isLoadingExpenses.value = false 
        }
    }

    fun addGrocery(name: String, taskGiver: String) = repository?.addGrocery(GroceryItem(name = name, taskGiver = taskGiver))
    fun toggleGrocery(id: String) {
        var updatedItem: GroceryItem? = null
        _groceries.update { current ->
            current.map { item ->
                if (item.id == id) {
                    updatedItem = item.copy(isChecked = !item.isChecked, completedAt = if (!item.isChecked) System.currentTimeMillis() else 0L)
                    updatedItem!!
                } else item
            }
        }
        updatedItem?.let { repository?.updateGrocery(it) }
    }
    fun deleteGrocery(id: String) = repository?.deleteGrocery(id)

    fun addChore(name: String, priority: Priority, assignedTo: String, taskGiver: String) = repository?.addChore(Chore(name = name, priority = priority.name, assignedTo = assignedTo, taskGiver = taskGiver))
    fun toggleChore(id: String) {
        var updatedItem: Chore? = null
        _chores.update { current ->
            current.map { item ->
                if (item.id == id) {
                    updatedItem = item.copy(isChecked = !item.isChecked, completedAt = if (!item.isChecked) System.currentTimeMillis() else 0L)
                    updatedItem!!
                } else item
            }
        }
        updatedItem?.let { repository?.updateChore(it) }
    }
    fun deleteChore(id: String) = repository?.deleteChore(id)

    fun addExpense(name: String, cost: Double, paidBy: String, splitAmong: List<String>) = repository?.addExpense(ExpenseItem(name = name, cost = cost, paidBy = paidBy, splitAmong = splitAmong, type = "EXPENSE", timestamp = System.currentTimeMillis()))
    fun addSettlement(paidBy: String, paidTo: String, cost: Double) = repository?.addExpense(ExpenseItem(name = "Settlement", cost = cost, paidBy = paidBy, paidTo = paidTo, type = "SETTLEMENT", timestamp = System.currentTimeMillis()))
    
    fun updateHouseholdName(newName: String) = repository?.updateHouseholdName(newName)
    fun removeMember(memberEmail: String, currentMembers: List<String>) = repository?.removeMember(memberEmail, currentMembers)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        enableEdgeToEdge()
        setContent { MyApplicationTheme { HouseholdSyncApp() } }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Home)
    object Tasks : Screen("tasks", "Tasks", Icons.Filled.Checklist)
    object Expenses : Screen("expenses", "Expenses", Icons.Filled.AttachMoney)
}

val bottomNavItems = listOf(Screen.Dashboard, Screen.Tasks, Screen.Expenses)

@Composable
fun FirebaseErrorScreen(isDark: Boolean) {
    Box(modifier = Modifier.fillMaxSize().background(bentoBg(isDark)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.Error, contentDescription = "Error", tint = Color.Red, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Firebase Configuration Error", style = MaterialTheme.typography.titleLarge, color = bentoTextMain(isDark))
            Text("Please connect to Firebase and add google-services.json", color = bentoTextSub(isDark), textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun AuthScreen(isDark: Boolean, onAuthSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    
    Column(
        modifier = Modifier.fillMaxSize().background(bentoBg(isDark)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Household Sync", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = BentoCardShape,
            border = BorderStroke(1.dp, bentoBorder(isDark)),
            colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Crossfade(targetState = isLogin, animationSpec = tween(500), label = "auth_crossfade") { loginMode ->
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(if (loginMode) "Login" else "Create Account", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!loginMode) {
                        OutlinedTextField(
                            value = name, 
                            onValueChange = { name = it }, 
                            label = { Text("Name", color = bentoTextSub(isDark)) },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Name") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark),
                                focusedBorderColor = bentoAccentBlue(isDark), focusedLeadingIconColor = bentoAccentBlue(isDark)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    OutlinedTextField(
                        value = email, 
                        onValueChange = { email = it }, 
                        label = { Text("Email", color = bentoTextSub(isDark)) },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = bentoTextMain(isDark), 
                            unfocusedTextColor = bentoTextMain(isDark),
                            focusedBorderColor = bentoAccentBlue(isDark),
                            focusedLeadingIconColor = bentoAccentBlue(isDark)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password, 
                        onValueChange = { password = it }, 
                        label = { Text("Password", color = bentoTextSub(isDark)) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(), singleLine = true, 
                        visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = bentoTextMain(isDark), 
                            unfocusedTextColor = bentoTextMain(isDark),
                            focusedBorderColor = bentoAccentBlue(isDark),
                            focusedLeadingIconColor = bentoAccentBlue(isDark),
                            focusedTrailingIconColor = bentoAccentBlue(isDark)
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank() || (!loginMode && name.isBlank())) return@Button
                            isLoading = true
                            if (loginMode) {
                                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) onAuthSuccess() else Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build()
                                        task.result?.user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                            isLoading = false
                                            onAuthSuccess()
                                        }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = bentoBlueText(isDark), contentColor = if (isDark) Color.Black else Color.White)
                    ) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else Text(if (loginMode) "Sign In" else "Sign Up")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = { isLogin = !isLogin }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(if (loginMode) "Don't have an account? Sign up" else "Already have an account? Login", color = bentoAccentBlue(isDark))
                    }
                }
            }
        }
    }
}

@Composable
fun HouseholdOnboardingScreen(isDark: Boolean, onBack: () -> Unit, onComplete: (String) -> Unit) {
    var joinCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    Box(modifier = Modifier.fillMaxSize().background(bentoBg(isDark))) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = bentoTextMain(isDark))
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Text("Setup Household", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = BentoCardShape,
            border = BorderStroke(1.dp, bentoBorder(isDark)),
            colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        isLoading = true
                        val newCode = UUID.randomUUID().toString().substring(0, 6).uppercase()
                        val householdData = hashMapOf("code" to newCode, "createdAt" to System.currentTimeMillis(), "createdBy" to auth.currentUser?.email, "name" to "My Household", "members" to listOf(auth.currentUser?.email))
                        db.collection("households").document(newCode).set(householdData).addOnCompleteListener {
                            isLoading = false
                            if (it.isSuccessful) onComplete(newCode)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bentoBlueText(isDark))
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp)) else Text("Create New Household", color = Color.White)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("OR", color = bentoTextSub(isDark), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = joinCode, onValueChange = { joinCode = it.uppercase() }, label = { Text("Enter Room Code", color = bentoTextSub(isDark)) },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (joinCode.length == 6) {
                            isLoading = true
                            db.collection("households").document(joinCode).get().addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful && task.result?.exists() == true) {
                                    val currentMembers = task.result?.get("members") as? List<String> ?: emptyList()
                                    val email = auth.currentUser?.email ?: ""
                                    if (email !in currentMembers) {
                                        db.collection("households").document(joinCode).update("members", currentMembers + email)
                                    }
                                    onComplete(joinCode)
                                }
                                else Toast.makeText(context, "Invalid Room Code", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bentoPurpleBg(isDark), contentColor = bentoPurpleText(isDark))
                ) { Text("Join Household") }
            }
        }
    }
}


    }
@Composable
fun HouseholdSyncApp() {
    val context = LocalContext.current
    val isFirebaseInitialized = remember {
        try { FirebaseApp.getInstance(); true } catch (e: Exception) { false }
    }
    val prefs = remember { context.getSharedPreferences("household_sync_prefs", Context.MODE_PRIVATE) }
    var isDarkMode by remember { mutableStateOf(prefs.getBoolean("is_dark_mode", false)) }
    var currencySymbol by remember { mutableStateOf(prefs.getString("currency", "$") ?: "$") }
    var language by remember { mutableStateOf(prefs.getString("language", "English") ?: "English") }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> }
    )
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && 
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (!isFirebaseInitialized) {
        FirebaseErrorScreen(isDarkMode)
        return
    }

    var householdId by remember { mutableStateOf(prefs.getString("household_id", null)) }
    val auth = FirebaseAuth.getInstance()
    var currentUser by remember { mutableStateOf(auth.currentUser) }
    var showOnboarding by remember { mutableStateOf(false) }

    if (currentUser == null) {
        AuthScreen(isDarkMode) { currentUser = auth.currentUser }
    } else if (showOnboarding) {
        HouseholdOnboardingScreen(isDarkMode, onBack = { showOnboarding = false }) { newHouseholdId ->
            prefs.edit().putString("household_id", newHouseholdId).apply()
            householdId = newHouseholdId
            showOnboarding = false
        }
    } else if (householdId == null) {
        HouseSelectorScreen(
            isDark = isDarkMode,
            onHouseSelected = { selectedId ->
                prefs.edit().putString("household_id", selectedId).apply()
                householdId = selectedId
            },
            onAddNew = { showOnboarding = true }
        )
    } else {
        val viewModel = remember { HouseholdViewModel() }
        LaunchedEffect(householdId) { viewModel.initialize(householdId!!) { title, message -> showNotification(context, title, message) } }
        
        MainAppContent(
            viewModel = viewModel,
            isDarkMode = isDarkMode,
            currencySymbol = currencySymbol,
            language = language,
            householdId = householdId!!,
            onThemeToggle = { 
                isDarkMode = it
                prefs.edit().putBoolean("is_dark_mode", it).apply()
            },
            onCurrencyChange = {
                currencySymbol = it
                prefs.edit().putString("currency", it).apply()
            },
            onLanguageChange = {
                language = it
                prefs.edit().putString("language", it).apply()
            },
            onBackToSelector = {
                prefs.edit().remove("household_id").apply()
                householdId = null
            },
            onLogout = {
                auth.signOut()
                prefs.edit().remove("household_id").apply()
                householdId = null
                currentUser = null
            }
        )
    }
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
fun MainAppContent(viewModel: HouseholdViewModel, isDarkMode: Boolean, currencySymbol: String, language: String, householdId: String, onThemeToggle: (Boolean) -> Unit, onCurrencyChange: (String) -> Unit, onLanguageChange: (String) -> Unit, onBackToSelector: () -> Unit, onLogout: () -> Unit) {
    CompositionLocalProvider(LocalLanguage provides language) {
    val navController = run { rememberNavController() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val isSettingsRoute = currentRoute == "settings"
    var showCalculator by remember { mutableStateOf(false) }
    var showMembers by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMsg by viewModel.error.collectAsState()

    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = bentoBg(isDarkMode),
        topBar = {
            TopAppBar(
                title = {
                    val title = if (isSettingsRoute) getString("Settings") else getString(bottomNavItems.find { it.route == currentRoute }?.title ?: "Household Sync")
                    Text(title, fontWeight = FontWeight.SemiBold, color = bentoTextMain(isDarkMode))
                },
                navigationIcon = {
                    if (isSettingsRoute) IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = bentoTextMain(isDarkMode)) }
                    else if (currentRoute == Screen.Dashboard.route) IconButton(onClick = onBackToSelector) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Houses", tint = bentoTextMain(isDarkMode)) }
                },
                actions = {
                    if (!isSettingsRoute) {
                        IconButton(onClick = { showMembers = true }) { Icon(Icons.Filled.Group, contentDescription = "Members", tint = bentoTextMain(isDarkMode)) }
                        IconButton(onClick = { showCalculator = true }) { Icon(Icons.Filled.Calculate, contentDescription = "Calculator", tint = bentoTextMain(isDarkMode)) }
                        IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = bentoTextMain(isDarkMode)) }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bentoBg(isDarkMode))
            )
        },
        bottomBar = {
            if (!isSettingsRoute) {
                NavigationBar(containerColor = bentoCardBg(isDarkMode), contentColor = bentoTextSub(isDarkMode), tonalElevation = 8.dp) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = getString(screen.title)) },
                            label = { Text(getString(screen.title), fontSize = 10.sp) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = bentoBlueText(isDarkMode), selectedTextColor = bentoBlueText(isDarkMode),
                                indicatorColor = bentoBlueBg(isDarkMode),
                                unselectedIconColor = bentoTextSub(isDarkMode).copy(alpha=0.5f), unselectedTextColor = bentoTextSub(isDarkMode).copy(alpha=0.5f)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Dashboard.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel, isDarkMode, householdId) }
            composable(Screen.Tasks.route) { TasksScreen(viewModel, isDarkMode) }
            composable(Screen.Expenses.route) { ExpensesScreen(viewModel, currencySymbol, isDarkMode) }
            composable("settings") { SettingsScreen(viewModel, isDarkMode, currencySymbol, LocalLanguage.current, householdId, onThemeToggle, onCurrencyChange, onLanguageChange, onLogout) }
        }
    }

    if (showCalculator) {
        CalculatorBottomSheet(isDarkMode) { showCalculator = false }
    }
    if (showMembers) {
        MembersBottomSheet(viewModel, isDarkMode, householdId, onLeaveHouse = onBackToSelector) { showMembers = false }
    }
    }
}

@Composable
fun DashboardScreen(viewModel: HouseholdViewModel, isDark: Boolean, householdId: String) {
    val groceries by viewModel.groceries.collectAsState()
    val chores by viewModel.chores.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val household by viewModel.household.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Welcome Card
        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("${getString("Room Code")}: $householdId", style = MaterialTheme.typography.labelMedium, color = bentoTextSub(isDark))
                Spacer(modifier = Modifier.height(4.dp))
                val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                val greeting = when (currentHour) {
                    in 5..11 -> "Good morning"
                    in 12..16 -> "Good afternoon"
                    in 17..20 -> "Good evening"
                    else -> "Good night"
                }
                val houseName = household?.name ?: "House"
                Text("${getString(greeting)}, ${if(houseName=="House") getString("House") else houseName}!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Upcoming Tasks Widget
        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(getString("Household Overview"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = BentoInnerShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoBlueBg(isDark))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(getString("Active Tasks"), style = MaterialTheme.typography.labelMedium, color = bentoBlueText(isDark).copy(alpha = 0.7f))
                        Text("${groceries.count { !it.isChecked } + chores.count { !it.isChecked }} ${getString("Pending")}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = bentoBlueText(isDark))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Expense Chart Widget
        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text(getString("Spending Trends (6 Months)"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
                Spacer(modifier = Modifier.height(24.dp))
                
                val cal = java.util.Calendar.getInstance()
                val currentMonth = cal.get(java.util.Calendar.MONTH)
                val currentYear = cal.get(java.util.Calendar.YEAR)
                
                val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                val last6Months = mutableListOf<String>()
                val monthlyTotals = FloatArray(6) { 0f }
                
                for (i in 5 downTo 0) {
                    var m = currentMonth - i
                    var y = currentYear
                    if (m < 0) { m += 12; y -= 1 }
                    last6Months.add(monthNames[m])
                }
                
                expenses.filter { it.type == "EXPENSE" }.forEach { exp ->
                    if (exp.timestamp > 0) {
                        cal.timeInMillis = exp.timestamp
                        val m = cal.get(java.util.Calendar.MONTH)
                        val y = cal.get(java.util.Calendar.YEAR)
                        
                        var index = -1
                        for (i in 0..5) {
                            var cm = currentMonth - (5 - i)
                            var cy = currentYear
                            if (cm < 0) { cm += 12; cy -= 1 }
                            if (m == cm && y == cy) { index = i; break }
                        }
                        if (index != -1) {
                            monthlyTotals[index] += exp.cost.toFloat()
                        }
                    }
                }
                
                val maxSpending = monthlyTotals.maxOrNull()?.coerceAtLeast(10f) ?: 10f
                val primaryColor = bentoAccentBlue(isDark)
                val labelColor = bentoTextSub(isDark)
                
                Row(modifier = Modifier.fillMaxWidth().height(160.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                    for (i in 0..5) {
                        val heightFraction = monthlyTotals[i] / maxSpending
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                            Text("$${monthlyTotals[i].toInt()}", style = MaterialTheme.typography.labelSmall, color = labelColor, fontSize = 9.sp, modifier = Modifier.padding(bottom = 4.dp))
                            Box(modifier = Modifier.width(32.dp).fillMaxHeight(heightFraction.coerceAtLeast(0.01f)).background(primaryColor, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(last6Months[i], style = MaterialTheme.typography.bodySmall, color = labelColor, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: HouseholdViewModel, isDark: Boolean) {
    val groceries by viewModel.groceries.collectAsState()
    val chores by viewModel.chores.collectAsState()
    val isLoadingGroceries by viewModel.isLoadingGroceries.collectAsState()
    val isLoadingChores by viewModel.isLoadingChores.collectAsState()
    
    var newTaskName by remember { mutableStateOf("") }
    
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Unknown"
    val taskGiverName = FirebaseAuth.getInstance().currentUser?.displayName ?: currentUserEmail.substringBefore("@")

    Column(modifier = Modifier.fillMaxSize()) {
        
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTaskName, onValueChange = { newTaskName = it }, label = { Text(getString("New Task"), color = bentoTextSub(isDark)) },
                modifier = Modifier.weight(1f), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = bentoBorder(isDark), unfocusedBorderColor = bentoBorder(isDark), focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { 
                    if (newTaskName.isNotBlank()) {
                        viewModel.addChore(newTaskName, Priority.Medium, currentUserEmail, taskGiverName)
                        newTaskName = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = bentoBlueText(isDark), contentColor = if (isDark) Color.Black else Color.White)
            ) { Text(getString("Add")) }
        }

        if (isLoadingGroceries && isLoadingChores) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = bentoAccentBlue(isDark)) }
        } else {
            val combinedList = mutableListOf<Any>()
            combinedList.addAll(groceries)
            combinedList.addAll(chores)
            
            val (completedTasks, activeTasks) = combinedList.partition { 
                (it as? GroceryItem)?.isChecked == true || (it as? Chore)?.isChecked == true 
            }
            
            val flatList = mutableListOf<Any>()
            flatList.addAll(activeTasks)
            if (completedTasks.isNotEmpty()) {
                flatList.add("HEADER_COMPLETED")
                flatList.addAll(completedTasks)
            }

            val tasksListState = rememberLazyListState()
            LazyColumn(state = tasksListState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp)) {
                item(key = "dummy_top_anchor") {
                    Spacer(modifier = Modifier.height(1.dp))
                }
                
                val renderItem: @Composable androidx.compose.foundation.lazy.LazyItemScope.(Any) -> Unit = { item ->
                    when (item) {
                        is GroceryItem -> {
                            Card(
                                modifier = Modifier.animateItem().fillMaxWidth().padding(vertical = 6.dp).clickable { viewModel.toggleGrocery(item.id) },
                                shape = BentoCardShape, colors = CardDefaults.cardColors(containerColor = if (item.isChecked) bentoBg(isDark) else bentoBlueBg(isDark)), border = if(item.isChecked) BorderStroke(1.dp, bentoBorder(isDark)) else null
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).alpha(if (item.isChecked) 0.5f else 1f), verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item.isChecked, onCheckedChange = null, colors = CheckboxDefaults.colors(checkedColor = bentoBlueText(isDark), uncheckedColor = bentoBlueText(isDark).copy(alpha = 0.5f), checkmarkColor = bentoBlueBg(isDark)))
                                    Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                        Text(text = item.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textDecoration = if (item.isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null, color = if (item.isChecked) bentoTextSub(isDark) else bentoBlueText(isDark))
                                        if (item.taskGiver.isNotBlank()) {
                                            Text(text = "${getString("Added by:")} ${item.taskGiver}", style = MaterialTheme.typography.bodySmall, color = if (item.isChecked) bentoTextSub(isDark) else bentoBlueText(isDark).copy(alpha=0.7f))
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteGrocery(item.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = if (item.isChecked) bentoTextSub(isDark) else bentoBlueText(isDark))
                                    }
                                }
                            }
                        }
                        is Chore -> {
                            Card(modifier = Modifier.animateItem().fillMaxWidth().padding(vertical = 6.dp).clickable { viewModel.toggleChore(item.id) }, shape = BentoCardShape, colors = CardDefaults.cardColors(containerColor = if (item.isChecked) bentoBg(isDark) else bentoPurpleBg(isDark)), border = if (item.isChecked) BorderStroke(1.dp, bentoBorder(isDark)) else null) {
                                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).alpha(if (item.isChecked) 0.5f else 1f), verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item.isChecked, onCheckedChange = null, colors = CheckboxDefaults.colors(checkedColor = bentoPurpleText(isDark), uncheckedColor = bentoPurpleText(isDark).copy(alpha = 0.5f), checkmarkColor = bentoPurpleBg(isDark)))
                                    Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                        Text(text = item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textDecoration = if (item.isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null, color = if(item.isChecked) bentoTextSub(isDark) else bentoPurpleText(isDark), modifier = Modifier.padding(bottom = 2.dp))
                                        Text(text = "${getString("Assigned to:")} ${item.assignedTo.substringBefore("@")}", style = MaterialTheme.typography.bodySmall, color = if(item.isChecked) bentoTextSub(isDark) else bentoPurpleText(isDark).copy(alpha = 0.7f))
                                        if (item.taskGiver.isNotBlank()) {
                                            Text(text = "${getString("Added by:")} ${item.taskGiver}", style = MaterialTheme.typography.bodySmall, color = if(item.isChecked) bentoTextSub(isDark) else bentoPurpleText(isDark).copy(alpha = 0.7f))
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteChore(item.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = if (item.isChecked) bentoTextSub(isDark) else bentoPurpleText(isDark))
                                    }
                                }
                            }
                        }
                    }
                }
                
                items(flatList, key = { item -> 
                    when (item) {
                        is String -> item
                        is GroceryItem -> item.id
                        is Chore -> item.id
                        else -> item.hashCode()
                    }
                }) { item -> 
                    if (item is String) {
                        Text(getString("Completed"), style = MaterialTheme.typography.titleMedium, color = bentoTextSub(isDark), modifier = Modifier.animateItem().padding(top = 16.dp, bottom = 8.dp))
                    } else {
                        renderItem(item)
                    }
                }
            }
        }
    }
}
@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
fun ExpensesScreen(viewModel: HouseholdViewModel, currencySymbol: String, isDark: Boolean) {
    val expenses by viewModel.expenses.collectAsState()
    val household by viewModel.household.collectAsState()
    val isLoading by viewModel.isLoadingExpenses.collectAsState()
    var itemName by remember { mutableStateOf("") }
    var totalCostStr by remember { mutableStateOf("") }
    var paidByStr by remember { mutableStateOf("") }
    
    val groceries by viewModel.groceries.collectAsState()
    val chores by viewModel.chores.collectAsState()
    
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Unknown"
    val currentUser = currentUserEmail.substringBefore("@")
    
    val members = remember(expenses, groceries, chores) {
        val m = mutableSetOf(currentUser)
        expenses.forEach { 
            if (it.paidBy.isNotBlank()) m.add(it.paidBy)
            if (it.paidTo.isNotBlank()) m.add(it.paidTo)
            m.addAll(it.splitAmong)
        }
        groceries.forEach { if (it.taskGiver.isNotBlank()) m.add(it.taskGiver) }
        chores.forEach { 
            if (it.taskGiver.isNotBlank()) m.add(it.taskGiver)
            if (it.assignedTo.isNotBlank()) m.add(it.assignedTo.substringBefore("@"))
        }
        m.toList().sorted()
    }

    if (paidByStr.isBlank() && members.isNotEmpty()) {
        paidByStr = currentUser
    }

    var selectedSplitMembers by remember(members) { mutableStateOf(members.toSet()) }
    var expandedPaidBy by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)) {
        item {
            Text(getString("Expense Splitter"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = itemName, onValueChange = { itemName = it }, label = { Text(getString("Item Name"), color = bentoTextSub(isDark)) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = bentoBorder(isDark), unfocusedBorderColor = bentoBorder(isDark), focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = totalCostStr, onValueChange = { totalCostStr = it }, label = { Text(getString("Cost"), color = bentoTextSub(isDark)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Text(currencySymbol, color = bentoTextMain(isDark)) },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = bentoBorder(isDark), unfocusedBorderColor = bentoBorder(isDark), focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ExposedDropdownMenuBox(expanded = expandedPaidBy, onExpandedChange = { expandedPaidBy = !expandedPaidBy }) {
                        OutlinedTextField(
                            value = paidByStr, onValueChange = {}, readOnly = true,
                            label = { Text(getString("Who Paid?"), color = bentoTextSub(isDark)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaidBy) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = bentoBorder(isDark), unfocusedBorderColor = bentoBorder(isDark), focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                        )
                        ExposedDropdownMenu(expanded = expandedPaidBy, onDismissRequest = { expandedPaidBy = false }) {
                            members.forEach { member ->
                                DropdownMenuItem(text = { Text(member) }, onClick = { paidByStr = member; expandedPaidBy = false })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(getString("Split among:"), style = MaterialTheme.typography.bodySmall, color = bentoTextSub(isDark))
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        members.forEach { member ->
                            val isSelected = selectedSplitMembers.contains(member)
                            FilterChip(
                                selected = isSelected,
                                onClick = { 
                                    val newSet = selectedSplitMembers.toMutableSet()
                                    if (isSelected) newSet.remove(member) else newSet.add(member)
                                    if (newSet.isNotEmpty()) selectedSplitMembers = newSet
                                },
                                label = { Text(member) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = bentoBlueBg(isDark), selectedLabelColor = bentoBlueText(isDark),
                                    labelColor = bentoTextSub(isDark)
                                ),
                                border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = bentoBorder(isDark))
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val cost = totalCostStr.toDoubleOrNull()
                            if (itemName.isNotBlank() && cost != null && selectedSplitMembers.isNotEmpty()) { 
                                viewModel.addExpense(itemName, cost, paidByStr, selectedSplitMembers.toList())
                                itemName = ""
                                totalCostStr = "" 
                            }
                        },
                        modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = bentoGrayBg(isDark), contentColor = bentoTextMain(isDark))
                    ) { Text(getString("Add Expense")) }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = bentoAccentBlue(isDark)) }
            }
        } else {
            val balances = mutableMapOf<String, Double>()
            members.forEach { balances[it] = 0.0 }
            
            expenses.forEach { exp ->
                if (exp.type == "SETTLEMENT") {
                    balances[exp.paidBy] = (balances[exp.paidBy] ?: 0.0) + exp.cost
                    balances[exp.paidTo] = (balances[exp.paidTo] ?: 0.0) - exp.cost
                } else {
                    val splitAmount = exp.cost / exp.splitAmong.size
                    balances[exp.paidBy] = (balances[exp.paidBy] ?: 0.0) + exp.cost
                    exp.splitAmong.forEach { user ->
                        balances[user] = (balances[user] ?: 0.0) - splitAmount
                    }
                }
            }
            
            val debtors = balances.filterValues { it < -0.01 }.mapValues { -it.value }.toMutableMap()
            val creditors = balances.filterValues { it > 0.01 }.toMutableMap()
            
            data class Debt(val debtor: String, val creditor: String, val amount: Double)
            val debtsToSettle = mutableListOf<Debt>()
            
            while (debtors.isNotEmpty() && creditors.isNotEmpty()) {
                val debtor = debtors.maxByOrNull { it.value }
                val creditor = creditors.maxByOrNull { it.value }
                if (debtor == null || creditor == null) break
                val amount = minOf(debtor.value, creditor.value)
                
                debtsToSettle.add(Debt(debtor.key, creditor.key, amount))
                
                val newDebtorVal = debtor.value - amount
                if (newDebtorVal < 0.01) debtors.remove(debtor.key) else debtors[debtor.key] = newDebtorVal
                
                val newCreditorVal = creditor.value - amount
                if (newCreditorVal < 0.01) creditors.remove(creditor.key) else creditors[creditor.key] = newCreditorVal
            }
            
            if (debtsToSettle.isNotEmpty()) {
                item {
                    Text(getString("Balances"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            debtsToSettle.forEach { debt ->
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("${debt.debtor} ${getString("owes")} ${debt.creditor} $currencySymbol${"%.2f".format(debt.amount)}", color = bentoTextMain(isDark), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                    
                                    Button(onClick = { viewModel.addSettlement(debt.debtor, debt.creditor, debt.amount) }, colors = ButtonDefaults.buttonColors(containerColor = bentoAccentBlue(isDark)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp), modifier = Modifier.height(32.dp)) {
                                        Text(getString("Settle Up"), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (expenses.isNotEmpty()) {
                val personTotals = mutableMapOf<String, Double>()
                expenses.filter { it.type == "EXPENSE" }.forEach { exp ->
                    personTotals[exp.paidBy] = (personTotals[exp.paidBy] ?: 0.0) + exp.cost
                }
                
                if (personTotals.isNotEmpty()) {
                    item {
                        Text(getString("Total Spending by Person"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 8.dp))
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val total = personTotals.values.sum()
                                val colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0), Color(0xFFF44336), Color(0xFFE91E63))
                                var startAngle = -90f
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                            personTotals.values.forEachIndexed { index, amount ->
                                                val sweepAngle = ((amount / total) * 360f).toFloat()
                                                drawArc(color = colors[index % colors.size], startAngle = startAngle, sweepAngle = sweepAngle, useCenter = true)
                                                startAngle += sweepAngle
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        personTotals.entries.forEachIndexed { index, entry ->
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                                Box(modifier = Modifier.size(10.dp).background(colors[index % colors.size], CircleShape))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("${entry.key}: $currencySymbol${"%.2f".format(entry.value)}", style = MaterialTheme.typography.bodySmall, color = bentoTextMain(isDark))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text(getString("Transaction History"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 8.dp))
                }
                items(expenses.sortedByDescending { it.timestamp }) { exp ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            if (exp.type == "SETTLEMENT") {
                                Text(getString("Settlement"), fontWeight = FontWeight.SemiBold, color = bentoAccentBlue(isDark))
                                Text("${exp.paidBy} paid ${exp.paidTo}", style = MaterialTheme.typography.bodySmall, color = bentoTextSub(isDark))
                            } else {
                                Text(exp.name, fontWeight = FontWeight.SemiBold, color = bentoTextMain(isDark))
                                Text("${getString("Paid by:")} ${exp.paidBy} • ${getString("Split among")} ${exp.splitAmong.size}", style = MaterialTheme.typography.bodySmall, color = bentoTextSub(isDark))
                            }
                        }
                        Text("$currencySymbol${"%.2f".format(exp.cost)}", fontWeight = FontWeight.Bold, color = if (exp.type == "SETTLEMENT") bentoAccentBlue(isDark) else bentoTextMain(isDark))
                    }
                    Divider(color = bentoBorder(isDark))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: HouseholdViewModel, isDark: Boolean, currencySymbol: String, language: String, householdId: String, onThemeToggle: (Boolean) -> Unit, onCurrencyChange: (String) -> Unit, onLanguageChange: (String) -> Unit, onLogout: () -> Unit) {
    val household by viewModel.household.collectAsState()
    val context = LocalContext.current
    
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val email = currentUser?.email ?: ""
    var displayName by remember { mutableStateOf(currentUser?.displayName ?: email.substringBefore("@")) }
    var editingName by remember { mutableStateOf(false) }
    var newDisplayName by remember { mutableStateOf(displayName) }
    
    val initials = displayName.take(2).uppercase()
    var editingHouseName by remember { mutableStateOf(false) }
    var newHouseName by remember { mutableStateOf(household?.name ?: "") }
    
    val isAdmin = household?.createdBy == email
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(bentoBlueBg(isDark)), contentAlignment = Alignment.Center) {
                    Text(initials, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = bentoBlueText(isDark))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    if (editingName) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(value = newDisplayName, onValueChange = { newDisplayName = it }, modifier = Modifier.weight(1f), singleLine = true)
                            IconButton(onClick = { 
                                val updates = com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(newDisplayName).build()
                                currentUser?.updateProfile(updates)?.addOnCompleteListener {
                                    if (it.isSuccessful) displayName = newDisplayName
                                    editingName = false
                                }
                            }) { Icon(Icons.Filled.Check, contentDescription = "Save", tint = bentoAccentBlue(isDark)) }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
                            IconButton(onClick = { editingName = true; newDisplayName = displayName }) { Icon(Icons.Filled.Edit, contentDescription = "Edit Name", tint = bentoTextSub(isDark)) }
                        }
                    }
                    Text(email, style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                    Text("${getString("Role: Admin").takeIf { isAdmin } ?: getString("Role: Member")}", style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (isAdmin) {
            Text(getString("Admin Controls"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 8.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(getString("Household Name"), style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                    if (editingHouseName) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(value = newHouseName, onValueChange = { newHouseName = it }, modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.updateHouseholdName(newHouseName); editingHouseName = false }) { Icon(Icons.Filled.Check, contentDescription = "Save", tint = bentoAccentBlue(isDark)) }
                            IconButton(onClick = { editingHouseName = false }) { Icon(Icons.Filled.Close, contentDescription = "Cancel", tint = Color.Red) }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(household?.name ?: "My Household", style = MaterialTheme.typography.bodyLarge, color = bentoTextMain(isDark), fontWeight = FontWeight.SemiBold)
                            IconButton(onClick = { editingHouseName = true; newHouseName = household?.name ?: "" }) { Icon(Icons.Filled.Edit, contentDescription = "Edit Name", tint = bentoTextSub(isDark)) }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if(isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode, contentDescription = null, tint = bentoTextMain(isDark))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(getString("Dark Mode"), style = MaterialTheme.typography.bodyLarge, color = bentoTextMain(isDark))
                }
                Switch(checked = isDark, onCheckedChange = onThemeToggle)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(getString("Preferences"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.padding(bottom = 8.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = BentoCardShape, border = BorderStroke(1.dp, bentoBorder(isDark)), colors = CardDefaults.cardColors(containerColor = bentoCardBg(isDark))) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                var expandedLanguage by remember { mutableStateOf(false) }
                val languages = listOf("English", "Spanish", "French", "German", "Japanese", "Hindi", "Bengali")
                
                Text(getString("Language"), style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                ExposedDropdownMenuBox(expanded = expandedLanguage, onExpandedChange = { expandedLanguage = !expandedLanguage }) {
                    OutlinedTextField(
                        value = language, onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLanguage) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                    )
                    ExposedDropdownMenu(expanded = expandedLanguage, onDismissRequest = { expandedLanguage = false }) {
                        languages.forEach { lang ->
                            DropdownMenuItem(text = { Text(lang) }, onClick = { onLanguageChange(lang); expandedLanguage = false })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                var expandedCurrency by remember { mutableStateOf(false) }
                val currencies = listOf("$", "€", "£", "₹", "¥")
                Text(getString("Currency"), style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                ExposedDropdownMenuBox(expanded = expandedCurrency, onExpandedChange = { expandedCurrency = !expandedCurrency }) {
                    OutlinedTextField(
                        value = currencySymbol, onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCurrency) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = bentoTextMain(isDark), unfocusedTextColor = bentoTextMain(isDark))
                    )
                    ExposedDropdownMenu(expanded = expandedCurrency, onDismissRequest = { expandedCurrency = false }) {
                        currencies.forEach { cur ->
                            DropdownMenuItem(text = { Text(cur) }, onClick = { onCurrencyChange(cur); expandedCurrency = false })
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.2f), contentColor = Color.Red),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(getString("Logout"), fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text("Version 2.1.1", style = MaterialTheme.typography.bodySmall, color = bentoTextSub(isDark), modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersBottomSheet(viewModel: HouseholdViewModel, isDark: Boolean, householdId: String, onLeaveHouse: () -> Unit, onDismiss: () -> Unit) {
    val household by viewModel.household.collectAsState()
    val context = LocalContext.current
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val isAdmin = household?.createdBy == email
    
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = bentoCardBg(isDark)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp)) {
            Text("Members & Room Code", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().background(bentoBg(isDark), RoundedCornerShape(12.dp)).padding(16.dp)) {
                Column {
                    Text(getString("Room Code"), style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark))
                    Text(householdId, style = MaterialTheme.typography.bodyLarge, color = bentoTextMain(isDark), fontWeight = FontWeight.SemiBold)
                }
                val roomCodeLabel = getString("Room Code")
                val codeCopiedLabel = getString("Code copied")
                IconButton(onClick = { 
                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText(roomCodeLabel, householdId)
                    clipboard.setPrimaryClip(clip)
                    android.widget.Toast.makeText(context, codeCopiedLabel, android.widget.Toast.LENGTH_SHORT).show()
                }) { Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = bentoAccentBlue(isDark)) }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(getString("Members"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
            Spacer(modifier = Modifier.height(8.dp))
            
            household?.members?.forEach { member ->
                val displayName = member.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(displayName, style = MaterialTheme.typography.bodyLarge, color = bentoTextMain(isDark))
                        if (household?.createdBy == member) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.background(bentoAccentBlue(isDark).copy(alpha=0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("Admin", color = bentoAccentBlue(isDark), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    if (isAdmin && member != email) {
                        IconButton(onClick = { viewModel.removeMember(member, household?.members ?: emptyList()) }) { 
                            Icon(Icons.Filled.Delete, contentDescription = "Remove", tint = Color.Red) 
                        }
                    }
                }
            }
            if (!isAdmin && household?.members?.contains(email) == true) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.removeMember(email, household?.members ?: emptyList())
                        onLeaveHouse()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White)
                ) { Text(getString("Leave House")) }
            }
        }
    }
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun HouseSelectorScreen(isDark: Boolean, onHouseSelected: (String) -> Unit, onAddNew: () -> Unit) {
    var households by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var houseToManage by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val email = auth.currentUser?.email ?: return
    
    LaunchedEffect(Unit) {
        db.collection("households").whereArrayContains("members", email).get().addOnCompleteListener { task ->
            isLoading = false
            if (task.isSuccessful) {
                households = task.result?.documents?.mapNotNull { doc -> 
                    doc.data?.plus("id" to doc.id)
                } ?: emptyList()
            }
        }
    }
    
    Column(modifier = Modifier.fillMaxSize().background(bentoBg(isDark)).padding(24.dp)) {
        Text("Your Houses", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark))
        Spacer(modifier = Modifier.height(24.dp))
        
        if (isLoading) {
            CircularProgressIndicator(color = bentoAccentBlue(isDark), modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(households.size) { index ->
                    val house = households[index]
                    val name = house["name"] as? String ?: "Household"
                    val id = house["id"] as? String ?: ""
                    val houseCardColors = if (isDark) listOf(Color(0xFF2C3E50), Color(0xFF8E44AD), Color(0xFF27AE60), Color(0xFFD35400), Color(0xFFC0392B), Color(0xFF2980B9), Color(0xFF16A085), Color(0xFFF39C12)) else listOf(Color(0xFFFFB3BA), Color(0xFFFFDFBA), Color(0xFFFFFFBA), Color(0xFFBAFFC9), Color(0xFFBAE1FF), Color(0xFFE2B6FF), Color(0xFFFFB6C1), Color(0xFFA0E8AF))
                    val houseColor = houseCardColors[index % houseCardColors.size]
                    Card(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).combinedClickable(onClick = { onHouseSelected(id) }, onLongClick = { houseToManage = house }),
                        shape = BentoCardShape,
                        colors = CardDefaults.cardColors(containerColor = houseColor),
                        border = BorderStroke(1.dp, bentoBorder(isDark))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
                
                item {
                    val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                    val borderColor = bentoTextSub(isDark).copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(BentoCardShape).clickable { onAddNew() }.drawBehind { drawRoundRect(color = borderColor, style = stroke, cornerRadius = CornerRadius(16.dp.toPx())) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Add, contentDescription = "Add New", tint = bentoTextSub(isDark).copy(alpha = 0.5f), modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Add New", style = MaterialTheme.typography.bodyMedium, color = bentoTextSub(isDark).copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
    houseToManage?.let { house ->
        val id = house["id"] as? String ?: ""
        val name = house["name"] as? String ?: "Household"
        val createdBy = house["createdBy"] as? String ?: ""
        val isAdmin = createdBy == email
        AlertDialog(
            onDismissRequest = { houseToManage = null },
            title = { Text("Manage $name", color = bentoTextMain(isDark)) },
            text = { Text(if (isAdmin) "Are you sure you want to delete this house for everyone?" else "Are you sure you want to leave this house?", color = bentoTextSub(isDark)) },
            containerColor = bentoBg(isDark),
            confirmButton = {
                TextButton(onClick = {
                    if (isAdmin) {
                        db.collection("households").document(id).delete().addOnSuccessListener {
                            households = households.filter { it["id"] != id }
                            houseToManage = null
                        }
                    } else {
                        val currentMembers = house["members"] as? List<String> ?: emptyList()
                        db.collection("households").document(id).update("members", currentMembers - email).addOnSuccessListener {
                            households = households.filter { it["id"] != id }
                            houseToManage = null
                        }
                    }
                }) { Text(if (isAdmin) "Delete" else "Leave", color = Color(0xFFFF5252)) }
            },
            dismissButton = {
                TextButton(onClick = { houseToManage = null }) { Text("Cancel", color = bentoAccentBlue(isDark)) }
            }
        )
    }
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
fun CalculatorBottomSheet(isDark: Boolean, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = bentoCardBg(isDark), dragHandle = { BottomSheetDefaults.DragHandle() }) {
        CalculatorUI(isDark)
    }
}

@Composable
fun CalculatorUI(isDark: Boolean) {
    var display by remember { mutableStateOf("0") }
    var operand by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var startNewInput by remember { mutableStateOf(true) }

    val buttons = listOf(
        listOf("7", "8", "9", "/"), listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"), listOf("C", "0", "=", "+")
    )

    val onAction = { action: String ->
        when (action) {
            "C" -> { display = "0"; operand = null; operator = null; startNewInput = true }
            "+", "-", "*", "/" -> {
                val current = display.toDoubleOrNull() ?: 0.0
                if (operand != null && operator != null && !startNewInput) {
                    val result = calculate(operand!!, current, operator!!)
                    display = formatResult(result)
                    operand = result
                } else operand = current
                operator = action
                startNewInput = true
            }
            "=" -> {
                val current = display.toDoubleOrNull() ?: 0.0
                if (operand != null && operator != null) {
                    display = formatResult(calculate(operand!!, current, operator!!))
                    operand = null; operator = null; startNewInput = true
                }
            }
            else -> {
                if (startNewInput) { display = action; startNewInput = false }
                else display = if (display == "0") action else display + action
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp)) {
        Text(display, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = bentoTextMain(isDark), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        Spacer(modifier = Modifier.height(24.dp))
        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { btn ->
                    val displayBtn = when(btn) { "*" -> "×"; "/" -> "÷"; else -> btn }
                    val isAction = btn in listOf("/", "*", "-", "+", "=")
                    Button(
                        onClick = { onAction(btn) }, modifier = Modifier.weight(1f).aspectRatio(1f), shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if(isAction) bentoBlueBg(isDark) else bentoGrayBg(isDark), contentColor = if(isAction) bentoBlueText(isDark) else bentoTextMain(isDark)),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) { Text(displayBtn, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun calculate(op1: Double, op2: Double, operator: String) = when (operator) { "+" -> op1 + op2; "-" -> op1 - op2; "*" -> op1 * op2; "/" -> if (op2 != 0.0) op1 / op2 else 0.0; else -> op2 }
fun formatResult(result: Double) = if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()



fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Household Notifications"
        val descriptionText = "Notifications for chores and expenses"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("HOUSEHOLD_CHANNEL", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotification(context: Context, title: String, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && 
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        return
    }
    
    val builder = NotificationCompat.Builder(context, "HOUSEHOLD_CHANNEL")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        
    val notificationManager = NotificationManagerCompat.from(context)
    try {
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
    } catch (e: SecurityException) {
        // Ignored
    }
}
