# Impl�mentation Emitter

Une impl�mentation inspir�e du C#, o� les �v�nements sont des propri�t�s.

**Forces**
- Simple � utiliser, les �v�nements sont des attributs
- Fonctionne avec les expressions lamba, ce qui permet d'utiliser un pointeur sur m�thode pour �tre listener
- Utilisation des annotations pour plus de simplicit�
- Tr�s g�n�rique, fonctionne par d�couverte des annotations
- Performance (si on n'utilise pas les annotations)

**Faiblesses**
- Les �v�nements d'un objet ne peuvent �tre d�clar�s dans son interface
- Performance (utilisation de la reflexion si on utilise les annotations)
- On ne peut plus unsubsribe les pointeurs sur m�thode