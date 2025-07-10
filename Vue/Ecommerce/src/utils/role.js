export function hasRole(payload, targetRole) {
  if (
    !payload.roles ||
    !Array.isArray(payload.roles) ||
    payload.roles.length === 0
  ) {
    return false;
  }

  const rolesString = payload.roles[0];
  const cleanRoles = rolesString
    .replace(/\[|\]/g, "")
    .split(",")
    .map((role) => role.trim());

  return cleanRoles.includes(targetRole);
}

export function cleanRole(payload) {
  if (
    !payload.roles ||
    !Array.isArray(payload.roles) ||
    payload.roles.length === 0
  ) {
    return false;
  }

  const rolesString = payload.roles[0]; // 假設 payload.roles 是 ['[ADMIN, USER]']

  // 去除 [ 和 ]，並分割成陣列，並去除每個角色的前後空白
  const cleanRoles = rolesString
    .replace(/\[|\]/g, "") // 移除 []
    .split(",") // 用逗號分割字串成陣列
    .map((role) => role.trim()); // 去除每個角色的前後空白

  return cleanRoles;
}
