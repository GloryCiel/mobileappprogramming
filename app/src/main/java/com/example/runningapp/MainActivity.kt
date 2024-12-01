package com.example.runningapp
import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import com.example.runningapp.data.database.AppDatabase
import com.example.runningapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CoroutineScope 생성
        val scope = CoroutineScope(Dispatchers.IO)
        
        scope.launch {
            // 데이터베이스 작업을 코루틴 내에서 실행
            AppDatabase.deleteDatabase(this@MainActivity)
            delay(200) // Thread.sleep 대신 delay 사용
            database = AppDatabase.getDatabase(this@MainActivity, scope)
        }

        // 바인딩 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation 설정
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val bottomNavView: BottomNavigationView = binding.bottomNavigation

        // bottom navigation view 에 controller 연결
        bottomNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}