package Donkey.Database.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
//    private List<ScreenRegister> screenList;
//    private List<Groupe> groupeList;

    //TODO
    //Faire le constructeur ect...
}
