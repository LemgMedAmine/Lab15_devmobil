package projet.fst.ma.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import projet.fst.ma.app.classes.Etudiant;
import projet.fst.ma.app.service.EtudiantService;

// Realise par Lemghili Mohammed Amine.
public class MainActivity extends Activity {

    private EditText nom;
    private EditText prenom;
    private Button add;

    private EditText id;
    private Button rechercher;
    private Button supprimer;
    private TextView res;

    void clear() {
        nom.setText("");
        prenom.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EtudiantService es = new EtudiantService(this);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        add = findViewById(R.id.bn);

        id = findViewById(R.id.id);
        rechercher = findViewById(R.id.load);
        supprimer = findViewById(R.id.delete);
        res = findViewById(R.id.res);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomText = nom.getText().toString().trim();
                String prenomText = prenom.getText().toString().trim();

                if (nomText.isEmpty() || prenomText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Saisir le nom et le prenom", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.create(new Etudiant(nomText, prenomText));
                clear();
                res.setText("Resultat de recherche");
                logEtudiants(es);

                Toast.makeText(MainActivity.this, "Etudiant ajoute", Toast.LENGTH_SHORT).show();
            }
        });

        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer etudiantId = readId();
                if (etudiantId == null) {
                    res.setText("");
                    return;
                }

                Etudiant e = es.findById(etudiantId);
                if (e == null) {
                    res.setText("Aucun etudiant trouve");
                    Toast.makeText(MainActivity.this, "Etudiant introuvable", Toast.LENGTH_SHORT).show();
                    return;
                }

                res.setText(e.getNom() + " " + e.getPrenom());
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer etudiantId = readId();
                if (etudiantId == null) {
                    return;
                }

                Etudiant e = es.findById(etudiantId);
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Aucun etudiant a supprimer", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.delete(e);
                res.setText("Resultat de recherche");
                logEtudiants(es);
                Toast.makeText(MainActivity.this, "Etudiant supprime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Integer readId() {
        String txt = id.getText().toString().trim();
        if (txt.isEmpty()) {
            Toast.makeText(MainActivity.this, "Saisir un id", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Id invalide", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void logEtudiants(EtudiantService es) {
        for (Etudiant e : es.findAll()) {
            Log.d(String.valueOf(e.getId()), e.getNom() + " " + e.getPrenom());
        }
    }
}
