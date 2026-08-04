enum AuthRole {
  owner,
  teknisi;

  String get label => switch (this) {
        AuthRole.owner => 'Owner',
        AuthRole.teknisi => 'Teknisi',
      };

  static AuthRole? fromName(String value) {
    for (final role in AuthRole.values) {
      if (role.name == value) return role;
    }
    return null;
  }
}

class AuthSession {
  const AuthSession({required this.role, required this.signedInAt});

  final AuthRole role;
  final DateTime signedInAt;

  Map<String, Object?> toJson() => {
        'role': role.name,
        'signedInAt': signedInAt.toIso8601String(),
      };

  static AuthSession? fromJson(Map<String, Object?> json) {
    final roleName = json['role'] as String?;
    final signedInAtText = json['signedInAt'] as String?;
    final role = roleName == null ? null : AuthRole.fromName(roleName);
    final signedInAt = signedInAtText == null ? null : DateTime.tryParse(signedInAtText);
    if (role == null || signedInAt == null) return null;
    return AuthSession(role: role, signedInAt: signedInAt);
  }
}
