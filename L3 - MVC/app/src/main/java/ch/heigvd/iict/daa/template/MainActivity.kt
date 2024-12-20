package ch.heigvd.iict.daa.template
/**
 * Nom du fichier : MainActivity.kt
 * Description    : Activité principale de l'application qui gère l'interface utilisateur
 *                  et les interactions.
 * Auteur         : Bugna, Slimani & Steiner
 * Date           : 17 octobre 2024
 */

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.daa.labo3.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etDateNaissance: EditText
    private lateinit var btnDatePicker: ImageButton
    private lateinit var etNom: EditText
    private lateinit var etPrenom: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCommentaires: EditText
    private lateinit var rgOccupation: RadioGroup
    private lateinit var spinnerNationalite: Spinner
    private lateinit var btnOk: Button
    private lateinit var btnAnnuler: Button
    private lateinit var layoutStudentInfo: LinearLayout
    private lateinit var layoutWorkerInfo: LinearLayout
    private lateinit var layoutComplementaryInfo: LinearLayout
    private lateinit var spinnerSecteur: Spinner
    private lateinit var etEcole: EditText
    private lateinit var etAnnee: EditText
    private lateinit var etEntreprise : EditText
    private lateinit var etExperience: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        initializeViews()

        // Configuration du Spinner
        setupSpinner()

        // Configuration du DatePicker
        setupDatePicker()

        // Configuration des boutons
        setupButtons()

        // Configuration des boutons Etudiant et Employé
        setupOccupationRadioGroup()

        fillStudentField()
    }

    private fun initializeViews() {
        etNom = findViewById(R.id.etNom)
        etPrenom = findViewById(R.id.etPrenom)
        etDateNaissance = findViewById(R.id.etDateNaissance)
        btnDatePicker = findViewById(R.id.btnDatePicker)
        spinnerNationalite = findViewById(R.id.spinnerNationalite)
        rgOccupation = findViewById(R.id.rgOccupation)
        etEmail = findViewById(R.id.etEmail)
        etCommentaires = findViewById(R.id.etCommentaires)
        btnOk = findViewById(R.id.btnOk)
        btnAnnuler = findViewById(R.id.btnAnnuler)
        layoutStudentInfo = findViewById(R.id.layoutStudentInfo)
        layoutWorkerInfo = findViewById(R.id.layoutWorkerInfo)
        layoutComplementaryInfo = findViewById(R.id.layoutComplementaryInfo)
        spinnerSecteur = findViewById(R.id.spinnerSecteur)
        etAnnee = findViewById(R.id.etAnneeDiplome)
        etEcole = findViewById(R.id.etEcole)
        etEntreprise = findViewById(R.id.etEntreprise)
        etExperience = findViewById(R.id.etExperience)

        layoutStudentInfo.visibility = View.GONE
        layoutWorkerInfo.visibility = View.GONE
    }

    private fun setupSpinner() {
        // Configuration du Spinner pour la nationalité
        val nationalityAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerNationalite.adapter = nationalityAdapter

        // Configuration du Spinner pour le secteur
        val sectorAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.sectors,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }


        spinnerSecteur.adapter = sectorAdapter // Assigner l'adaptateur au Spinner

        // Optionnel : gérer la sélection du Spinner pour la nationalité
        spinnerNationalite.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {  // Si ce n'est pas "Sélectionner"
                    val nationalite = parent?.getItemAtPosition(position).toString()
                    // Faire quelque chose avec la nationalité sélectionnée
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ne rien faire
            }
        }

        // Optionnel : gérer la sélection du Spinner pour le secteur
        spinnerSecteur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {  // Si ce n'est pas "Sélectionner"
                    val secteur = parent?.getItemAtPosition(position).toString()
                    // Faire quelque chose avec le secteur sélectionné
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ne rien faire
            }
        }
    }

    private fun setupDatePicker() {
        // Rendre le champ non éditable
        etDateNaissance.isFocusable = false
        etDateNaissance.isClickable = true

        val dateClickListener = View.OnClickListener { showDatePicker() }

        // Configurer les deux façons d'ouvrir le DatePicker
        etDateNaissance.setOnClickListener(dateClickListener)
        btnDatePicker.setOnClickListener(dateClickListener)
    }

    /**
     * Affiche un DatePickerDialog permettant à l'utilisateur de sélectionner une date.
     * La méthode récupère également la date actuelle (ou la date précédemment saisie)
     * pour initialiser le calendrier à la bonne date dans le dialogue.
     */
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        // Si une date est déjà sélectionnée, l'utiliser
        val currentDate = etDateNaissance.text.toString()
        if (currentDate.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                sdf.parse(currentDate)?.let {
                    calendar.time = it
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        // Affiche le Dialog de sélection de date
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val date = String.format(Locale.FRANCE, "%02d/%02d/%d",
                    day, month + 1, year)
                etDateNaissance.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupButtons() {
        btnOk.setOnClickListener {
            // Vérifier si tous les champs requis sont remplis
            if (validateFields()) {
                // Traiter les données du formulaire
                processForm()
            }
        }

        btnAnnuler.setOnClickListener {
            // Réinitialiser tous les champs
            resetForm()
        }
    }

    private fun validateFields(): Boolean {
        // Vérifier que les champs obligatoires sont remplis
        when {
            etNom.text.isEmpty() -> {
                etNom.error = "Le nom est obligatoire"
                return false
            }
            etPrenom.text.isEmpty() -> {
                etPrenom.error = "Le prénom est obligatoire"
                return false
            }
            etDateNaissance.text.isEmpty() -> {
                Toast.makeText(this, "La date de naissance est obligatoire", Toast.LENGTH_SHORT).show()
                return false
            }
            spinnerNationalite.selectedItemPosition == 0 -> {
                Toast.makeText(this, "Veuillez sélectionner une nationalité", Toast.LENGTH_SHORT).show()
                return false
            }
            rgOccupation.checkedRadioButtonId == -1 -> {
                Toast.makeText(this, "Veuillez sélectionner une occupation", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun processForm() {
        // Récupérer toutes les valeurs
        val nom = etNom.text.toString()
        val prenom = etPrenom.text.toString()
        val dateNaissance = etDateNaissance.text.toString()
        val nationalite = spinnerNationalite.selectedItem.toString()

        val occupation = when (rgOccupation.checkedRadioButtonId) {
            R.id.rbEtudiant -> "Étudiant"
            R.id.rbEmploye -> "Employé"
            else -> ""
        }

        val email = etEmail.text.toString()
        val commentaires = etCommentaires.text.toString()



        // Création d'une instance de Student ou Worker
        val person = if (occupation == "Étudiant") {
            // Récupérer les données spécifiques aux étudiants
            val ecole = findViewById<EditText>(R.id.etEcole).text.toString()
            val anneeDiplome = findViewById<EditText>(R.id.etAnneeDiplome).text.toString().toIntOrNull() ?: 0 // Utiliser 0 si non valide

            Student(nom, prenom, parseDate(dateNaissance), nationalite, ecole, anneeDiplome, email, commentaires)
        } else {
            // Récupérer les données spécifiques aux employés
            val entreprise = findViewById<EditText>(R.id.etEntreprise).text.toString()
            val secteur = spinnerSecteur.selectedItem.toString() // Récupérer la valeur du Spinner
            val experience = findViewById<EditText>(R.id.etExperience).text.toString().toIntOrNull() ?: 0 // Utiliser 0 si non valide

            Worker(nom, prenom, parseDate(dateNaissance), nationalite, entreprise, secteur, experience, email, commentaires)
        }

        // Afficher l'objet créé dans les logs
        Log.d("MainActivity", person.toString())


        // Faire quelque chose avec les données (par exemple, les afficher dans un Toast)
        Toast.makeText(this, "Formulaire validé avec succès", Toast.LENGTH_SHORT).show()
    }


    private fun parseDate(date: String): Calendar {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        try {
            calendar.time = sdf.parse(date) ?: Date()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return calendar
    }

    private fun setupOccupationRadioGroup() {
        rgOccupation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbEtudiant -> {
                    layoutStudentInfo.visibility = View.VISIBLE
                    layoutWorkerInfo.visibility = View.GONE
                }
                R.id.rbEmploye -> {
                    layoutStudentInfo.visibility = View.GONE
                    layoutWorkerInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun resetForm() {
        etNom.text.clear()
        etPrenom.text.clear()
        etDateNaissance.text.clear()
        spinnerNationalite.setSelection(0)
        rgOccupation.clearCheck()
        etEmail.text.clear()
        etCommentaires.text.clear()
        etEcole.text.clear()
        etAnnee.text.clear()
        etExperience.text.clear()
        etEntreprise.text.clear()
        spinnerSecteur.setSelection(0)

        layoutStudentInfo.visibility = View.GONE
        layoutWorkerInfo.visibility = View.GONE
    }

    private fun fillStudentField() {
        val exampleStudent = Student(
            "Dreher",
            "Matthias",
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 1998)
                set(Calendar.MONTH, Calendar.APRIL)
                set(Calendar.DAY_OF_MONTH, 8)
            },
            "Allemande",
            "HEIG-VD",
            2023,
            "m.dreher@email.com",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        )

        // Remplissage des champs avec les valeurs de l'exemple d'étudiant
        etNom.setText(exampleStudent.name)
        etPrenom.setText(exampleStudent.firstName)
        etDateNaissance.setText(
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(exampleStudent.birthDay.time)
        )
        val nationalitePosition = (spinnerNationalite.adapter as ArrayAdapter<String>)
            .getPosition(exampleStudent.nationality)
        spinnerNationalite.setSelection(nationalitePosition)
        rgOccupation.check(R.id.rbEtudiant)
        etEmail.setText(exampleStudent.email)
        etCommentaires.setText(exampleStudent.remark)
        etEcole.setText(exampleStudent.university)
        etAnnee.setText(exampleStudent.graduationYear.toString())

        // Afficher le layout pour les informations de l'étudiant
        layoutStudentInfo.visibility = View.VISIBLE
        layoutWorkerInfo.visibility = View.GONE
    }
}