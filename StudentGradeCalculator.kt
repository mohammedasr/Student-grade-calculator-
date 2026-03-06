//IF REVIEWED BY TEAMMATE TYPE YES IN CAPITAL LETTER HERE
//MOHAMMED HAIBAI MOUSTAPHA ABOUBAKAR – STUDENT GRADE CALCULATOR

interface Gradable {
    fun calculateAverage(): Double
    fun getLetterGrade(): String
    fun getRemarks(): String
}

interface Displayable {
    fun displayInfo()
    fun displayResult()
}

interface Assessable {
    fun addScore(subject: String, score: Double)
    fun getScores(): Map<String, Double>
}

// BASE ABSTRACT CLASS

abstract class Person(
    val id: String,
    val name: String,
    val age: Int
) : Displayable {

    override fun displayInfo() {
        println("-----------------------------------")
        println("Student ID : $id")
        println("Name       : $name")
        println("Age        : $age")
    }
}

// ABSTRACT STUDENT CLASS

abstract class Student(
    id: String,
    name: String,
    age: Int,
    val yearLevel: Int
) : Person(id, name, age), Gradable, Assessable {

    protected val subjectScores = mutableMapOf<String, Double>()

    override fun addScore(subject: String, score: Double) {
        require(score in 0.0..100.0) { "Score must be between 0 and 100." }
        subjectScores[subject] = score
    }

    override fun getScores(): Map<String, Double> = subjectScores.toMap()

    override fun calculateAverage(): Double {
        if (subjectScores.isEmpty()) return 0.0
        return subjectScores.values.sum() / subjectScores.size
    }

    override fun getLetterGrade(): String {
        val avg = calculateAverage()
        return when (avg) {
            in 90.0..100.0 -> "A"
            in 80.0..89.99 -> "B"
            in 70.0..79.99 -> "C"
            in 60.0..69.99 -> "D"
            else -> "F"
        }
    }

    override fun getRemarks(): String {
        return when (getLetterGrade()) {
            "A" -> "Excellent"
            "B" -> "Good"
            "C" -> "Average"
            "D" -> "Below Average"
            else -> "Failed"
        }
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Year Level : Year $yearLevel")
        println("Category   : ${studentType()}")
    }

    override fun displayResult() {
        displayInfo()
        println("-----------------------------------")
        println("Subjects and Scores:")

        if (subjectScores.isEmpty()) {
            println("  No scores available.")
        } else {
            subjectScores.forEach { (subject, score) ->
                println("  %-20s : %.2f".format(subject, score))
            }
        }

        println("-----------------------------------")
        println("Average Score : %.2f".format(calculateAverage()))
        println("Letter Grade  : ${getLetterGrade()}")
        println("Remarks       : ${getRemarks()}")
        println("-----------------------------------\n")
    }

    abstract fun studentType(): String
}

// UNDERGRADUATE STUDENT

class UndergraduateStudent(
    id: String,
    name: String,
    age: Int,
    yearLevel: Int,
    val major: String
) : Student(id, name, age, yearLevel) {

    override fun studentType() = "Undergraduate Student"

    override fun displayInfo() {
        super.displayInfo()
        println("Major Field : $major")
    }
}

// GRADUATE STUDENT

class GraduateStudent(
    id: String,
    name: String,
    age: Int,
    yearLevel: Int,
    val thesis: String
) : Student(id, name, age, yearLevel) {

    override fun studentType() = "Graduate Student"

    // Graduate students have slightly higher passing requirement
    override fun getLetterGrade(): String {
        val avg = calculateAverage()
        return when (avg) {
            in 90.0..100.0 -> "A"
            in 80.0..89.99 -> "B"
            in 70.0..79.99 -> "C"
            in 65.0..69.99 -> "D"
            else -> "F"
        }
    }

    override fun displayInfo() {
        super.displayInfo()
        println("Research Topic : $thesis")
    }
}

// SCHOLARSHIP STUDENT

class ScholarshipStudent(
    id: String,
    name: String,
    age: Int,
    yearLevel: Int,
    val scholarshipName: String
) : Student(id, name, age, yearLevel) {

    override fun studentType() = "Scholarship Holder"

    // Scholarship condition
    fun isScholarshipMaintained(): Boolean = calculateAverage() >= 85.0

    override fun displayResult() {
        super.displayResult()

        val status =
            if (isScholarshipMaintained()) "Scholarship Maintained"
            else "Scholarship At Risk"

        println("Scholarship : $scholarshipName")
        println("Status      : $status")
        println("-----------------------------------\n")
    }
}

// GRADE REPORT MANAGER

class GradeReportManager : Displayable {

    private val students = mutableListOf<Student>()

    fun enrollStudent(student: Student) {
        students.add(student)
        println("Student Enrolled: ${student.name}")
    }

    fun generateAllReports() {
        println("\n========== STUDENT REPORTS ==========\n")

        if (students.isEmpty()) {
            println("No students found.")
            return
        }

        students.forEach { it.displayResult() }
    }

    fun getTopStudent(): Student? =
        students.maxByOrNull { it.calculateAverage() }

    override fun displayInfo() {
        println("Total Students: ${students.size}")
    }

    override fun displayResult() {
        displayInfo()

        val topStudent = getTopStudent()
        if (topStudent != null) {
            println(
                "Best Student: ${topStudent.name} with average %.2f (${topStudent.getLetterGrade()})"
                    .format(topStudent.calculateAverage())
            )
        }
    }
}

// MAIN PROGRAM

fun main() {

    val manager = GradeReportManager()

    val student1 = UndergraduateStudent(
        id = "U001",
        name = "Alice Johnson",
        age = 20,
        yearLevel = 2,
        major = "Computer Science"
    ).apply {
        addScore("Mathematics", 92.0)
        addScore("Programming", 88.0)
        addScore("Data Structures", 95.0)
        addScore("English", 85.0)
    }

    val student2 = GraduateStudent(
        id = "G001",
        name = "Bob Martinez",
        age = 25,
        yearLevel = 1,
        thesis = "Machine Learning in Healthcare"
    ).apply {
        addScore("Advanced Algorithms", 78.0)
        addScore("Research Methods", 82.0)
        addScore("Statistics", 74.0)
        addScore("Thesis Writing", 80.0)
    }

    val student3 = ScholarshipStudent(
        id = "S001",
        name = "Clara Lee",
        age = 29,
        yearLevel = 1,
        scholarshipName = "Dean's Excellence Award"
    ).apply {
        addScore("Biology", 90.0)
        addScore("Chemistry", 87.0)
        addScore("Wire Area Network", 83.0)
        addScore("CCNA1", 91.0)
    }

    val student4 = ScholarshipStudent(
        id = "S002",
        name = "David Kim",
        age = 21,
        yearLevel = 3,
        scholarshipName = "STEM Grant"
    ).apply {
        addScore("Calculus", 72.0)
        addScore("Physics", 68.0)
        addScore("Engineering", 75.0)
        addScore("Technical Writing", 70.0)
    }

    println("\n========== ENROLLMENT ==========")
    manager.enrollStudent(student1)
    manager.enrollStudent(student2)
    manager.enrollStudent(student3)
    manager.enrollStudent(student4)

    manager.generateAllReports()

    println("========== SUMMARY ==========")
    manager.displayResult()
    println("=================================\n")
}