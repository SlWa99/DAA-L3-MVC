#set page(
  header: align(right)[
    #set text(9pt)
    _#datetime.today().display()_
    #h(1fr)
    _DAA-L3-MVC Rapport_
  ],
  footer: [
    #set align(right)
    #set text(8pt)
    _Bugna, Slimani & Steiner_
    #h(1fr)
    _#counter(page).display(
      "1 / 1",
      both: true,
    )_
  ]
)

#align(center, text(20pt)[
  *DAA-L3-MVC*
])
 
\
_Auteurs : Bugna, Slimani & Steiner_
\
\
= Réponses aux questions

== Question 4.1
Pour le champ `remark`, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

=== Réponse :
Pour configurer correctement un champ texte multi-lignes comme `remark`, il est nécessaire de définir certains attributs dans le fichier XML. Voici comment s'assurer que le champ permet les retours à la ligne, le correcteur d'orthographe et que la taille s'ajuste correctement:

- Activer l'entrée multi-lignes: `android:inputType="textMultiLine"` permet d'autoriser les retours à la ligne.
- Correcteur orthographique: L'attribut `android:inputType` inclue aussi le correcteur orthographique `textAutoCorrect`.
- Ajustement de la hauteur: Utiliser `android:layout_height` avec une hauteur `fixe` ou `wrap_content` pour s'adapter aux lignes supplémentaires.
- Défilement vertical (si la hauteur est limitée): `android:scrollbars="vertical"`.

```xml
<EditText
    android:id="@+id/etCommentaires"
    android:layout_width="0dp"
    android:layout_height="60dp"
    android:layout_weight="0.6"
    android:inputType="textMultiLine|textAutoCorrect"
    android:gravity="top"
    android:scrollbars="vertical"
/>
```

== Question 4.2
Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple, mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on gérer cela au mieux ?

=== Réponse :
Pour afficher les dates en fonction de la langue de l'utilisateur (par exemple, « 12 juin 1996 » en français ou « June 12, 1996 » en anglais), il faut utiliser `DateFormat.getDateInstance()` avec différents styles de DateFormat. Cette méthode formatte les dates en fonction des paramètres régionaux de l’appareil.

Code présent dans Person.kt :
```kt
val dateFormatter = DateFormat.getDateInstance()
```

== Question 4.3
Veuillez choisir une question en fonction de votre choix d’implémentation :
- *a. Si vous avez utilisé le DatePickerDialog1 du SDK. En cas de rotation de l’écran du smartphone lorsque le dialogue est ouvert, une exception `android.view.WindowLeaked` sera présente dans les logs, à quoi est-elle due ?*
- _b. Si vous avez utilisé le MaterialDatePicker2 de la librairie Material. Est-il possible de limiter les dates sélectionnables dans le dialogue, en particulier pour une date de naissance il est peu probable d’avoir une personne née il y a plus de 110 ans ou à une date dans le futur.\ Comment pouvons-nous mettre cela en place ?_

=== Réponse :
L'exception `android.view.WindowLeaked` survient lorsque le `DatePickerDialog` est affiché et que l'activité ou le fragment qui l'a créé est détruite sans que le dialogue ne soit correctement annulé ou fermé. En cas de rotation de l’écran, Android recrée par défaut l'activité pour ajuster l'interface utilisateur à la nouvelle orientation. Pendant cette recréation, si un dialogue reste ouvert et qu'il n'a pas été correctement fermé, Android perçoit cela comme une fuite de fenêtre `WindowLeaked`, car l'activité d'origine est détruite, mais le dialogue peut rester attaché à une "vieille" référence de cette activité.

Cependant nous n'avons pas trouvé trace de cette erreur dans les logs. Les raisons suivantes semblent pouvoir l'expliquer en partie :

- Rotation Rapide ou Instantanée : Il est possible que la rotation soit assez rapide pour que le `DatePickerDialog` n’ait pas le temps de causer une fuite avant d’être automatiquement détruit. Dans les versions plus récentes d’Android, le système peut gérer cela plus efficacement en annulant le dialogue associé à une activité en cours de destruction.

- Absence de Références Persistantes : Le `DatePickerDialog` n’est pas stocké dans une variable de l’activité (comme une variable de classe), ce qui réduit les risques de fuite. Étant donné qu’il est instantanément créé et affiché dans `showDatePicker()` sans être retenu, il est automatiquement libéré avec l’activité dans de nombreux cas, ce qui évite potentiellement l'exception.

- Pas de Création Répétée en Boucle : Puisque `showDatePicker()` est appelé depuis un événement spécifique (un clic), le dialogue n'est pas créé de manière répétitive, réduisant ainsi le risque de fuite.  

Une autre solution consisterait à utiliser un `DialogFragment` pour encapsuler le `DatePickerDialog`, ce qui permet à Android de mieux gérer la recréation du dialogue lors des changements de configuration (comme la rotation de l’écran). En encapsulant le `DatePickerDialog` dans un `DialogFragment`, celui-ci persiste lors de la rotation et est recréé automatiquement avec l'activité, éliminant ainsi le problème de `WindowLeaked`. Il semble d'ailleurs que cela soit une bonne pratique d'android studio.

== Question 4.4
Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir, cf. Fig. 2. Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ?
Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?
_Hint : Le champ remark, multilignes, peut provoquer des effets de bords en fonction du clavier virtuel utilisé sur votre smartphone. Vous pouvez l’échanger avec le champ e-mail pour faciliter vos recherches concernant la réponse à cette question._

=== Réponse :
Oui pour contrôler l’ordre de navigation entre les champs de texte, il faut utiliser `android:nextFocusForward="@id/nextField"` sur chaque EditText pour définir le champ suivant à atteindre lorsque « Suivant » est pressé. Pour le dernier champ, il faut associer le bouton d’action du clavier à la soumission du formulaire en utilisant `android:imeOptions="actionDone"` et gérer l'événement `EditorActionListener`.

Dans le fichier activity_main.xml:
```xml
<EditText
    android:id="@+id/etEmail"
    android:nextFocusForward="@id/etCommentaires"
    android:imeOptions="actionNext"
/>

<EditText
    android:id="@+id/etCommentaires"
    android:imeOptions="actionDone"
/>
```

Et dans le fichier MainActivity.kt :
```kt
etCommentaires.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_DONE && validateFields()) {
        processForm() // appel à la gestion du submit par exemple
        true
    } else {
        false
    }
}
```

== Question 4.5
Pour les deux Spinners (nationalité et secteur d’activité), comment peut-on faire en sorte que le premier choix corresponde au choix null, affichant par exemple le label « Sélectionner » ? Comment peut-on gérer cette valeur pour ne pas qu’elle soit confondue avec une réponse ?

=== Réponse :
Pour afficher un choix par défaut (comme « Sélectionner ») dans les champs Spinner (par exemple, pour la nationalité), il faut ajouter une entrée par défaut à la position 0 dans la liste de l’adaptateur. Lors de la validation de la soumission du formulaire il faut vérifier si la sélection du spinner correspond à ce choix.

Par exemple :
```kt
val nationalities = listOf("Sélectionner", "Suisse", "Française", "Allemande")
val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nationalities)
spinnerNationalite.adapter = adapter

// Vérification de la sélection par défaut
// autre possibilitée : spinnerNationalite.selectedItemPosition == 0
if (spinnerNationalite.selectedItem.toString() == "Sélectionner") {
    // Gérer comme non sélectionné
}
```