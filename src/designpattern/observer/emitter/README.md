# Implémentation Emitter

Une implémentation inspirée du C#, où les événements sont des propriétés.

**Forces**
- Simple à utiliser, les événements sont des attributs
- Fonctionne avec les expressions lamba, ce qui permet d'utiliser un pointeur sur méthode pour être listener
- Utilisation des annotations pour plus de simplicité
- Très générique, fonctionne par découverte des annotations
- Performance (si on n'utilise pas les annotations)

**Faiblesses**
- Les événements d'un objet ne peuvent être déclarés dans son interface
- Performance (utilisation de la reflexion si on utilise les annotations)
- On ne peut plus unsubsribe les pointeurs sur méthode