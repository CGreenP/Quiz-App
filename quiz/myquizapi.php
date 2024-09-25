<?php
// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Database connection
$conn = mysqli_connect("localhost", "root", "", "my_quiz_db");

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Prepare SQL statement
$stmt = $conn->prepare("SELECT `question`, `option1`, `option2`, `option3`, `option4`, `correct_option` FROM `math_table`");

// Check if prepare was successful
if ($stmt === false) {
    die("Prepare failed: " . $conn->error);
}

// Execute the query
if (!$stmt->execute()) {
    die("Execute failed: " . $stmt->error);
}

// Bind results
if (!$stmt->bind_result($question, $option1, $option2, $option3, $option4, $correct_option)) {
    die("Binding results failed: " . $stmt->error);
}

// Create an empty array for questions
$questions_array = array();

// Fetch and store results
while ($stmt->fetch()) {
    $temp = array(
        'question' => $question,
        'option1' => $option1,
        'option2' => $option2,
        'option3' => $option3,
        'option4' => $option4,
        'correct_option' => $correct_option
    );
    $questions_array[] = $temp;
}

// Close statement and connection
$stmt->close();
$conn->close();

// Output JSON
header('Content-Type: application/json');
echo json_encode($questions_array);
?>