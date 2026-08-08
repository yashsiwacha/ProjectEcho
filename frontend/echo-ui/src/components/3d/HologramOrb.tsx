'use client';

import { useEffect, useRef } from 'react';
import * as THREE from 'three';

export default function HologramOrb({
  size = 220,
  verified = true,
  className = ''
}: {
  size?: number;
  verified?: boolean;
  className?: string;
}) {
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(45, 1, 0.1, 100);
    camera.position.z = 3.6;

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(size, size);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    container.appendChild(renderer.domElement);

    const primaryColor = verified ? 0x10b981 : 0xd4af37;
    const secondaryColor = verified ? 0x06b6d4 : 0xf6e05e;

    // 1. Core Sphere
    const coreGeo = new THREE.SphereGeometry(0.55, 32, 32);
    const coreMat = new THREE.MeshStandardMaterial({
      color: primaryColor,
      emissive: primaryColor,
      emissiveIntensity: 0.4,
      roughness: 0.1,
      metalness: 0.9,
      wireframe: true,
    });
    const core = new THREE.Mesh(coreGeo, coreMat);
    scene.add(core);

    // 2. Outer Wireframe Icosahedron
    const icoGeo = new THREE.IcosahedronGeometry(0.9, 1);
    const icoMat = new THREE.MeshBasicMaterial({
      color: secondaryColor,
      wireframe: true,
      transparent: true,
      opacity: 0.7,
    });
    const ico = new THREE.Mesh(icoGeo, icoMat);
    scene.add(ico);

    // 3. Gimbal Rings
    const ringGeo = new THREE.TorusGeometry(1.15, 0.015, 16, 64);
    const ringMat = new THREE.MeshBasicMaterial({
      color: primaryColor,
      transparent: true,
      opacity: 0.5,
    });

    const ring1 = new THREE.Mesh(ringGeo, ringMat);
    const ring2 = new THREE.Mesh(ringGeo, ringMat);
    ring2.rotation.x = Math.PI / 2;
    scene.add(ring1);
    scene.add(ring2);

    // 4. Sparkle Particles
    const pCount = 60;
    const pGeo = new THREE.BufferGeometry();
    const pPos = new Float32Array(pCount * 3);
    for (let i = 0; i < pCount * 3; i += 3) {
      const radius = 1.25 + Math.random() * 0.4;
      const theta = Math.random() * Math.PI * 2;
      const phi = Math.acos(Math.random() * 2 - 1);
      pPos[i] = radius * Math.sin(phi) * Math.cos(theta);
      pPos[i + 1] = radius * Math.sin(phi) * Math.sin(theta);
      pPos[i + 2] = radius * Math.cos(phi);
    }
    pGeo.setAttribute('position', new THREE.BufferAttribute(pPos, 3));
    const pMat = new THREE.PointsMaterial({
      color: 0xffffff,
      size: 0.035,
      transparent: true,
      opacity: 0.8,
    });
    const particles = new THREE.Points(pGeo, pMat);
    scene.add(particles);

    // 5. Lighting
    const pointLight = new THREE.PointLight(primaryColor, 3, 20);
    pointLight.position.set(2, 2, 3);
    scene.add(pointLight);

    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);

    let frameId: number;
    const clock = new THREE.Clock();

    const animate = () => {
      frameId = requestAnimationFrame(animate);
      const t = clock.getElapsedTime();

      core.rotation.y = t * 0.5;
      core.rotation.x = t * 0.3;

      ico.rotation.y = -t * 0.35;
      ico.rotation.z = t * 0.2;

      ring1.rotation.y = t * 0.8;
      ring1.rotation.x = Math.sin(t * 0.5) * 0.4;

      ring2.rotation.z = -t * 0.6;
      ring2.rotation.x = Math.PI / 2 + Math.cos(t * 0.5) * 0.4;

      particles.rotation.y = t * 0.15;

      renderer.render(scene, camera);
    };

    animate();

    return () => {
      cancelAnimationFrame(frameId);
      if (renderer.domElement && container.contains(renderer.domElement)) {
        container.removeChild(renderer.domElement);
      }
      renderer.dispose();
    };
  }, [size, verified]);

  return (
    <div className={`relative flex items-center justify-center ${className}`}>
      <div ref={containerRef} style={{ width: size, height: size }} />
      <div className="absolute inset-0 rounded-full radial-glow pointer-events-none" />
    </div>
  );
}
