export function Modal({ titulo, onCerrar, children }) {
  return (
    <div
      className="fixed inset-0 z-20 flex items-center justify-center bg-black/50 p-4"
      onClick={onCerrar}
    >
      <div
        className="w-full max-w-md rounded-lg bg-surface dark:bg-surface-dark p-6"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="mb-4 flex items-center justify-between">
          <h2 className="font-display text-lg font-bold text-[#1A2332] dark:text-[#F5F6F7]">{titulo}</h2>
          <button
            onClick={onCerrar}
            aria-label="Cerrar"
            className="text-muted dark:text-muted-dark hover:text-accent"
          >
            ✕
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}
