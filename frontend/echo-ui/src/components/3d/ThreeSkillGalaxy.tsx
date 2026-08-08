'use client';

import { useEffect, useRef, useState } from 'react';
import * as THREE from 'three';

interface SkillNode {
  id: string;
  name: string;
  category: string;
  tier: string;
  position: [number, number, number];
  connections: number[];
}

const DEFAULT_NODES: SkillNode[] = [
  { id: '1', name: 'Java 21 Architecture', category: 'Backend', tier: 'Tier 4', position: [0, 0.8, 0], connections: [1, 2, 4] },
  { id: '2', name: 'Spring Boot 3 DDD', category: 'Backend', tier: 'Tier 4', position: [-1.4, 0.4, 0.5], connections: [0, 3, 5] },
  { id: '3', name: 'Distributed Systems', category: 'Architecture', tier: 'Tier 4', position: [1.3, 0.5, -0.4], connections: [0, 6] },
  { id: '4', name: 'Kafka Event Streaming', category: 'Infrastructure', tier: 'Tier 3', position: [-1.1, -0.7, -0.8], connections: [1, 7] },
  { id: '5', name: 'React 19 & Next.js', category: 'Frontend', tier: 'Tier 4', position: [0.8, -0.6, 1.1], connections: [0, 8] },
  { id: '6', name: 'Three.js & WebGL', category: 'Creative Tech', tier: 'Tier 4', position: [1.6, -0.2, 0.3], connections: [4, 8] },
  { id: '7', name: 'PostgreSQL 16 & JPA', category: 'Database', tier: 'Tier 4', position: [-0.6, 1.2, -1.0], connections: [0, 2] },
  { id: '8', name: 'OWASP Security & RBAC', category: 'Security', tier: 'Tier 4', position: [-1.5, -0.3, 0.9], connections: [1, 3] },
  { id: '9', name: 'Explainable AI Reasoning', category: 'Intelligence', tier: 'Tier 4', position: [0.4, 1.4, 0.6], connections: [4, 5] },
];

export default function ThreeSkillGalaxy({
  onSelectNode,
  className = 'w-full h-[460px]'
}: {
  onSelectNode?: (node: SkillNode) => void;
  className?: string;
}) {
  const containerRef = useRef<HTMLDivElement>(null);
  const [selectedNode, setSelectedNode] = useState<SkillNode | null>(null);
  const [hoveredNode, setHoveredNode] = useState<SkillNode | null>(null);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const width = container.clientWidth || 800;
    const height = container.clientHeight || 460;

    // 1. Scene & Camera
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(50, width / height, 0.1, 1000);
    camera.position.z = 4.8;

    // 2. Renderer
    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(width, height);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    container.appendChild(renderer.domElement);

    // 3. Ambient Lighting & Point Lights
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.8);
    scene.add(ambientLight);

    const champagneLight = new THREE.PointLight(0xd4af37, 3, 50);
    champagneLight.position.set(2, 4, 3);
    scene.add(champagneLight);

    const cyanLight = new THREE.PointLight(0x06b6d4, 2.5, 50);
    cyanLight.position.set(-3, -2, -2);
    scene.add(cyanLight);

    // 4. Background Starfield Particles
    const starCount = 350;
    const starGeometry = new THREE.BufferGeometry();
    const starPositions = new Float32Array(starCount * 3);
    for (let i = 0; i < starCount * 3; i += 3) {
      starPositions[i] = (Math.random() - 0.5) * 12;
      starPositions[i + 1] = (Math.random() - 0.5) * 12;
      starPositions[i + 2] = (Math.random() - 0.5) * 10 - 2;
    }
    starGeometry.setAttribute('position', new THREE.BufferAttribute(starPositions, 3));
    const starMaterial = new THREE.PointsMaterial({
      color: 0xd4af37,
      size: 0.035,
      transparent: true,
      opacity: 0.6,
    });
    const starField = new THREE.Points(starGeometry, starMaterial);
    scene.add(starField);

    // 5. Constellation Group
    const constellationGroup = new THREE.Group();
    scene.add(constellationGroup);

    // Skill Spheres
    const nodeMeshes: THREE.Mesh[] = [];
    const sphereGeometry = new THREE.SphereGeometry(0.12, 24, 24);

    DEFAULT_NODES.forEach((node, index) => {
      const isChampagne = index % 2 === 0;
      const material = new THREE.MeshStandardMaterial({
        color: isChampagne ? 0xd4af37 : 0x10b981,
        emissive: isChampagne ? 0xb78a02 : 0x059669,
        emissiveIntensity: 0.6,
        roughness: 0.2,
        metalness: 0.8,
      });
      const mesh = new THREE.Mesh(sphereGeometry, material);
      mesh.position.set(...node.position);
      mesh.userData = { nodeIndex: index };
      constellationGroup.add(mesh);
      nodeMeshes.push(mesh);

      // Add a subtle outer glow halo ring
      const haloGeo = new THREE.RingGeometry(0.14, 0.18, 20);
      const haloMat = new THREE.MeshBasicMaterial({
        color: isChampagne ? 0xf6e05e : 0x34d399,
        side: THREE.DoubleSide,
        transparent: true,
        opacity: 0.35,
      });
      const halo = new THREE.Mesh(haloGeo, haloMat);
      halo.position.set(...node.position);
      halo.lookAt(camera.position);
      constellationGroup.add(halo);
    });

    // Constellation Lines
    const lineMaterial = new THREE.LineBasicMaterial({
      color: 0xd4af37,
      transparent: true,
      opacity: 0.25,
    });

    DEFAULT_NODES.forEach((node, i) => {
      node.connections.forEach((targetIndex) => {
        if (targetIndex > i && DEFAULT_NODES[targetIndex]) {
          const target = DEFAULT_NODES[targetIndex];
          const lineGeo = new THREE.BufferGeometry().setFromPoints([
            new THREE.Vector3(...node.position),
            new THREE.Vector3(...target.position),
          ]);
          const line = new THREE.Line(lineGeo, lineMaterial);
          constellationGroup.add(line);
        }
      });
    });

    // 6. Raycasting & Interaction
    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();
    let isDragging = false;
    let previousMousePosition = { x: 0, y: 0 };
    let targetRotationX = 0;
    let targetRotationY = 0;

    const onPointerDown = (e: MouseEvent) => {
      isDragging = true;
      previousMousePosition = { x: e.clientX, y: e.clientY };
    };

    const onPointerMove = (e: MouseEvent) => {
      const rect = container.getBoundingClientRect();
      mouse.x = ((e.clientX - rect.left) / width) * 2 - 1;
      mouse.y = -((e.clientY - rect.top) / height) * 2 + 1;

      if (isDragging) {
        const deltaX = e.clientX - previousMousePosition.x;
        const deltaY = e.clientY - previousMousePosition.y;
        targetRotationY += deltaX * 0.005;
        targetRotationX += deltaY * 0.005;
        previousMousePosition = { x: e.clientX, y: e.clientY };
      }

      // Check hover
      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(nodeMeshes);
      if (intersects.length > 0) {
        const nodeIdx = intersects[0].object.userData.nodeIndex;
        setHoveredNode(DEFAULT_NODES[nodeIdx]);
        container.style.cursor = 'pointer';
      } else {
        setHoveredNode(null);
        container.style.cursor = isDragging ? 'grabbing' : 'grab';
      }
    };

    const onPointerUp = (e: MouseEvent) => {
      isDragging = false;
      const rect = container.getBoundingClientRect();
      mouse.x = ((e.clientX - rect.left) / width) * 2 - 1;
      mouse.y = -((e.clientY - rect.top) / height) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(nodeMeshes);
      if (intersects.length > 0) {
        const nodeIdx = intersects[0].object.userData.nodeIndex;
        const clickedNode = DEFAULT_NODES[nodeIdx];
        setSelectedNode(clickedNode);
        if (onSelectNode) onSelectNode(clickedNode);
      }
    };

    container.addEventListener('mousedown', onPointerDown);
    window.addEventListener('mousemove', onPointerMove);
    window.addEventListener('mouseup', onPointerUp);

    // 7. Animation Loop
    let animationFrameId: number;
    let clock = new THREE.Clock();

    const animate = () => {
      animationFrameId = requestAnimationFrame(animate);
      const elapsedTime = clock.getElapsedTime();

      // Smooth damped rotation
      constellationGroup.rotation.y += (targetRotationY - constellationGroup.rotation.y) * 0.05 + 0.0015;
      constellationGroup.rotation.x += (targetRotationX - constellationGroup.rotation.x) * 0.05;

      starField.rotation.y = elapsedTime * 0.02;

      // Pulse nodes
      nodeMeshes.forEach((mesh, idx) => {
        const scale = 1 + Math.sin(elapsedTime * 2 + idx) * 0.08;
        mesh.scale.set(scale, scale, scale);
      });

      renderer.render(scene, camera);
    };

    animate();

    // 8. Resize Handler
    const handleResize = () => {
      if (!container) return;
      const newWidth = container.clientWidth;
      const newHeight = container.clientHeight;
      camera.aspect = newWidth / newHeight;
      camera.updateProjectionMatrix();
      renderer.setSize(newWidth, newHeight);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      cancelAnimationFrame(animationFrameId);
      container.removeEventListener('mousedown', onPointerDown);
      window.removeEventListener('mousemove', onPointerMove);
      window.removeEventListener('mouseup', onPointerUp);
      window.removeEventListener('resize', handleResize);
      if (renderer.domElement && container.contains(renderer.domElement)) {
        container.removeChild(renderer.domElement);
      }
      renderer.dispose();
    };
  }, [onSelectNode]);

  return (
    <div className={`relative rounded-2xl overflow-hidden glass-panel-glow ${className}`}>
      {/* 3D Canvas Host */}
      <div ref={containerRef} className="w-full h-full cursor-grab active:cursor-grabbing" />

      {/* Floating HUD Overlay */}
      <div className="absolute top-4 left-4 z-10 flex items-center gap-2 pointer-events-none">
        <span className="inline-block w-2.5 h-2.5 rounded-full bg-accent animate-ping" />
        <span className="text-xs font-mono font-semibold tracking-wider text-accent uppercase bg-background/80 px-2.5 py-1 rounded-md border border-accent/30 backdrop-blur-md">
          3D Competency Universe Active
        </span>
      </div>

      <div className="absolute bottom-4 left-4 z-10 text-[11px] text-muted-foreground font-mono bg-background/70 px-3 py-1.5 rounded-lg border border-border backdrop-blur-md pointer-events-none">
        🖱️ Drag to rotate 3D space · Click skill node to inspect
      </div>

      {/* Interactive Tooltip on Hover */}
      {hoveredNode && (
        <div className="absolute top-4 right-4 z-20 p-3.5 rounded-xl bg-card/95 border border-accent/40 shadow-2xl backdrop-blur-xl pointer-events-none animate-in fade-in zoom-in-95 duration-150 max-w-xs">
          <div className="flex items-center justify-between gap-3">
            <span className="text-xs font-bold text-foreground">{hoveredNode.name}</span>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 font-semibold border border-emerald-500/30">
              {hoveredNode.tier}
            </span>
          </div>
          <p className="text-[11px] text-muted-foreground mt-1">Domain: {hoveredNode.category}</p>
        </div>
      )}

      {/* Selected Node Details Drawer */}
      {selectedNode && (
        <div className="absolute bottom-4 right-4 z-20 p-4 rounded-xl bg-card/95 border border-border shadow-2xl backdrop-blur-xl max-w-xs flex flex-col gap-2">
          <div className="flex items-center justify-between">
            <h4 className="font-bold text-sm text-foreground">{selectedNode.name}</h4>
            <button
              onClick={() => setSelectedNode(null)}
              className="text-xs text-muted-foreground hover:text-foreground"
            >
              ✕
            </button>
          </div>
          <p className="text-xs text-muted-foreground">
            Cryptographically anchored skill verified against aggregate domain root proof graphs.
          </p>
          <div className="flex items-center justify-between pt-2 border-t border-border text-xs">
            <span className="text-accent font-semibold">{selectedNode.tier} Certified</span>
            <span className="font-mono text-[10px] text-muted-foreground">ID: #{selectedNode.id}</span>
          </div>
        </div>
      )}
    </div>
  );
}
