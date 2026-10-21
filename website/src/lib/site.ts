/** Links and copy shared by the landing page components. */
const base = import.meta.env.BASE_URL.replace(/\/$/, '');

/** Prefix a site path with the deploy base (/wallpanel-reborn). */
export const url = (path: string) => `${base}${path.startsWith('/') ? path : `/${path}`}`;

export const repo = 'https://github.com/beecho01/wallpanel-reborn';

export const links = {
  // Releases page rather than /releases/latest, which skips prereleases.
  download: `${repo}/releases`,
  releases: `${repo}/releases`,
  issues: `${repo}/issues`,
  github: repo,
  docs: url('/docs/'),
  gettingStarted: url('/docs/getting-started/'),
  mqtt: url('/docs/remote-control/mqtt-setup/'),
  commands: url('/docs/remote-control/commands/'),
  sensors: url('/docs/remote-control/sensors/'),
  limitations: url('/docs/limitations/'),
  privacy: url('/privacy-policy/'),
};

export const nav = [
  {label: 'Features', href: '#features'},
  {label: 'How it works', href: '#how-it-works'},
  {label: 'FAQ', href: '#faq'},
  {label: 'Docs', href: links.docs},
];
