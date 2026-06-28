import { NavLink, Outlet } from 'react-router-dom';

const ITEMS = [
  { to: '/admin', label: 'Resumen', exact: true },
  { to: '/admin/productos', label: 'Productos' },
  { to: '/admin/categorias', label: 'Categorias' },
  { to: '/admin/ordenes', label: 'Ordenes' },
];

export function AdminLayout() {
  return (
    <div className="mx-auto flex max-w-6xl gap-8 px-4 py-8">
      <aside className="w-48 shrink-0">
        <h2 className="mb-4 font-display text-sm font-bold uppercase tracking-wide text-muted dark:text-muted-dark">
          Panel admin
        </h2>
        <nav className="flex flex-col gap-1">
          {ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.exact}
              className={({ isActive }) =>
                `rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                  isActive
                    ? 'bg-accent text-white'
                    : 'text-muted dark:text-muted-dark hover:bg-muted/10'
                }`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>

      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  );
}
