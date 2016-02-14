# Impl�mentation Source

Une impl�mentation beaucoup plus �volu�e, qui se base sur la r�flexion.

**Features**
- Indice de priorit�, pour ordonner la propagation aux listeners
- Permet l'interruption de la propagation
- Gestion des exceptions lev�es par les listeners
- Redirection d'un EventDispatcher vers un autre (d�coration)
- M�thodes de trigger asynchrones
- Thread-safe avec le dispatcher EDT de Swing
- Logger

**Forces**
- Permet de sp�cifier des interfaces claires pour les events
- Encapsul�e : la classe EventDispatcher impl�mente la logique de broadcast, pas besoin de l'impl�menter
- Plus oblig� d'impl�menter une interface pour devenir listener, une m�me classe peut donc �tre listener de plusieurs events
- Enti�rement g�n�rique

**Faiblesses**
- Performances (utilisation de la Reflexion)
- Typage faible, le compilateur ne peut pas d�tecter les erreurs quand on utilise bind() pour pointer une m�thode
- Complexe � comprendre