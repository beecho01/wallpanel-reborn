// @ts-check
import {defineConfig} from 'astro/config';
import starlight from '@astrojs/starlight';

const repo = 'https://github.com/beecho01/wallpanel-reborn';

export default defineConfig({
  site: 'https://beecho01.github.io',
  base: '/wallpanel-reborn',
  integrations: [
    starlight({
      title: 'WallPanel Reborn',
      description: 'Turn any Android tablet into an always-on wall panel for your web dashboard.',
      logo: {src: './src/assets/mark.svg'},
      favicon: '/favicon.svg',
      social: [{icon: 'github', label: 'GitHub', href: repo}],
      editLink: {baseUrl: `${repo}/edit/main/website/`},
      customCss: ['@fontsource-variable/figtree', '@fontsource/fragment-mono/400.css', './src/styles/starlight.css'],
      sidebar: [
        {
          label: 'Start here',
          items: ['docs', 'docs/getting-started', 'docs/limitations'],
        },
        {
          label: 'Remote control (MQTT / HTTP)',
          items: ['docs/remote-control/mqtt-setup', 'docs/remote-control/commands', 'docs/remote-control/sensors'],
        },
        {
          label: 'Features',
          items: ['docs/screensaver', 'docs/video-streaming', 'docs/launch-external-apps'],
        },
        {
          label: 'Project',
          items: [
            'privacy-policy',
            {label: 'Releases', link: `${repo}/releases`, attrs: {target: '_blank'}},
            {label: 'Report an issue', link: `${repo}/issues`, attrs: {target: '_blank'}},
          ],
        },
      ],
    }),
  ],
});
