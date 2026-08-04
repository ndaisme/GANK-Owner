import '../../../dashboard/presentation/home_shell.dart';
import '../entities/auth_session.dart';

extension AuthRolePermissions on AuthRole {
  bool get hasFullAccess => this == AuthRole.owner;

  Set<MainTab> get allowedTabs => switch (this) {
        AuthRole.owner => MainTab.values.toSet(),
        AuthRole.teknisi => {MainTab.dashboard, MainTab.services, MainTab.inventory, MainTab.customers},
      };

  bool canAccess(MainTab tab) => allowedTabs.contains(tab);
}
