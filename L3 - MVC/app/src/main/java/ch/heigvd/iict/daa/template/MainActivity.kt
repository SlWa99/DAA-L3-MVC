package ch.heigvd.iict.daa.template

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser le Spinner
        val spinner: Spinner = findViewById(R.id.spinnerNationalite)

        // Créer un ArrayAdapter en utilisant le fichier de ressources de chaînes
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.nationalities, // Assurez-vous que ce tableau est défini dans res/values/strings.xml
            android.R.layout.simple_spinner_item
        )

        // Spécifier le layout à utiliser quand la liste d'options s'affiche
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Appliquer l'adapter à le Spinner
        spinner.adapter = adapter
    }
}