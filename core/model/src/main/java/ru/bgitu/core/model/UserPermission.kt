package ru.bgitu.core.model

enum class UserPermission(val nameId: Int) {
    AddHomework(R.string.perm_add_homework),
    EditSchedule(R.string.perm_change_schedule),
    BroadcastGroup(R.string.perm_broadcast_global),
    BroadcastFlow(R.string.perm_broadcast_flow),
    BroadcastGlobal(R.string.perm_broadcast_global),
    RevokePermissions(R.string.perm_revoke_permissions)
}