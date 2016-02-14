# Observer DesignPattern in Java

Several implementations of Observer Design Pattern in Java

### Implémentation Basique

Une implémentation très sommaire du design pattern correspond à sa stricte définition.
[Voir le code](https://github.com/rbello/Observer-DesignPattern-in-Java/tree/master/src/designpattern/observer/basic)

### Implémentation Dispatcher

Une version beaucoup plus évoluée, qui supporte la priorisation des listeners, l'arrêt de propagation, qui gère les exceptions levées par les listeners, et qui permet de rediriger les events d'un dispatcher vers un autre.
[Voir le code](https://github.com/rbello/Observer-DesignPattern-in-Java/tree/master/src/designpattern/observer/dispatcher)

### Implémentation Source

Une implémentation plus complexe et complète, qui supporte l'asynchrone.
[Voir le code](https://github.com/rbello/Observer-DesignPattern-in-Java/tree/master/src/designpattern/observer/source)