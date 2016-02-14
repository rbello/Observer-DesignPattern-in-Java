# Impl�mentation Basique

Cette impl�mentation tr�s sommaire du design pattern correspond � sa d�finition stricte, tel qu'on la retrouve sur [wikip�dia](https://fr.wikipedia.org/wiki/Observateur_%28patron_de_conception%29).

**Forces**
- Tr�s simple � impl�menter, et � comprendre
- G�n�rique

**Faiblesses**
- Les interfaces ne peuvent �tre impl�ment�es qu'une fois chacune, donc une classe ne peut pas �mettre plusieurs events
- L'utilisation de AObservable impose de modifier la hi�rarchie d'h�ritage
- Pour chaque type d'event on doit cr�er une classe