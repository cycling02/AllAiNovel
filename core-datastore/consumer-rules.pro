# Consumer rules for core-datastore module
# DataStore and SharedPreferences related rules

-keep class androidx.datastore.** { *; }
-keep class * implements androidx.datastore.core.Serializer { *; }

# Keep Preference keys
-keep class androidx.datastore.preferences.** { *; }

# Keep serialized classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
