# Impl�mentation Dispatcher

Une impl�mentation beaucoup plus �volu�e, qui se base sur la r�flexion.

La m�thode addListener() est remplac�e par bind() dans cette impl�mentation.

**Features**
- Indice de priorit�, pour ordonner la propagation aux listeners
- Permet l'interruption de la propagation
- Gestion des exceptions lev�es par les listeners
- Redirection d'un EventDispatcher vers un autre (d�coration)

**Forces**
- Relativement simple � comprendre
- Encapsul�e : la classe EventDispatcher impl�mente la logique de broadcast, pas besoin de l'impl�menter
- Plus oblig� d'impl�menter une interface pour devenir listener, une m�me classe peut donc �tre listener de plusieurs events
- G�n�rique

**Faiblesses**
- Performances (utilisation de la Reflexion)
- Typage faible, le compilateur ne peut pas d�tecter les erreurs quand on utilise bind() pour pointer une m�thode