package com.example.testdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testdialog.ui.theme.TestDialogTheme
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var studentAdapter: StudentAdapter
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentAdapter = StudentAdapter(
            students,
            onEditClick = { student, position ->
                showEditStudentDialog(student, position)
            },
            onDeleteClick = { student, position ->
                showDeleteStudentDialog(student, position)
            }
        )

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog(students, studentAdapter)
        }

    }
    private fun showAddStudentDialog(
        students: MutableList<StudentModel>,
        studentAdapter: StudentAdapter
    ) {
        // Inflate layout dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)

        // Create dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .create()

        // Get references to dialog views
        val editStudentName = dialogView.findViewById<EditText>(R.id.edit_student_name)
        val editStudentId = dialogView.findViewById<EditText>(R.id.edit_student_id)
        val btnAdd = dialogView.findViewById<Button>(R.id.btn_add)

        // Handle Add button click
        btnAdd.setOnClickListener {
            val name = editStudentName.text.toString().trim()
            val id = editStudentId.text.toString().trim()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                // Add new student to list and notify adapter
                students.add(StudentModel(name, id))
                studentAdapter.notifyItemInserted(students.size - 1)

                // Dismiss dialog
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
    private fun showEditStudentDialog(student: StudentModel, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val editName = dialogView.findViewById<EditText>(R.id.edit_student_name)
        val editId = dialogView.findViewById<EditText>(R.id.edit_student_id)

        // Gán dữ liệu của sinh viên vào EditText
        editName.setText(student.studentName)
        editId.setText(student.studentId)

        // Tạo Dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                // Cập nhật thông tin sinh viên
                val updatedName = editName.text.toString()
                val updatedId = editId.text.toString()

                if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
                    student.studentName = updatedName
                    student.studentId = updatedId
                    studentAdapter.notifyItemChanged(position) // Cập nhật giao diện
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showDeleteStudentDialog(student: StudentModel, position: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.studentName}?")
            .setPositiveButton("Delete") { _, _ ->
                // Lưu thông tin sinh viên bị xóa để khôi phục
                val deletedStudent = student
                val deletedPosition = position

                // Xóa sinh viên khỏi danh sách
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)

                // Hiển thị Snackbar với tùy chọn Undo
                Snackbar.make(findViewById(R.id.main), "${student.studentName} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        // Khôi phục sinh viên bị xóa
                        students.add(deletedPosition, deletedStudent)
                        studentAdapter.notifyItemInserted(deletedPosition)
                    }.show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}