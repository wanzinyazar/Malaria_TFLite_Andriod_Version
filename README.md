# Malaria_Andriod

Advancers.ai is a solution for malaria infected cell detection. This is the mobile version written in Java for Android. In the application we use SQLite for local data storage. We use TensorFlow Lite for Android for the machine learning component. The purpose of having locally stored ML models is so that our app can be used in places where there’s no internet and still be able to detect Malaria.

Our Android application utilizes Java programming language, SQLite database and built-in UX/UI from Android. The application uses Artificial Intelligence to effectively diagnose Malaria. The user will be able to take a picture of the smeared blood sample and the application will take the picture and run our offline TensorFlow Lite file and produce the results, showing the percentages of parasitized and uninfected cells found in the blood sample. The user will be able to save the patient’s data, such as their name and the generated results.
