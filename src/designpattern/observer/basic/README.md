# Implémentation Basique

Cette implémentation très sommaire du design pattern correspond à sa définition stricte, tel qu'on la retrouve sur [wikipédia](https://fr.wikipedia.org/wiki/Observateur_%28patron_de_conception%29).

**Forces**
- Très simple à implémenter, et à comprendre
- Générique

**Faiblesses**
- Les interfaces ne peuvent être implémentées qu'une fois chacune, donc une classe ne peut pas émettre plusieurs events
- L'utilisation de AObservable impose de modifier la hiérarchie d'héritage
- Pour chaque type d'event on doit créer une classe